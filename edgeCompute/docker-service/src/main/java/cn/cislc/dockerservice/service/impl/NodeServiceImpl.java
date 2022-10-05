package cn.cislc.dockerservice.service.impl;

import cn.cislc.dockerservice.entity.Node;
import cn.cislc.dockerservice.mapper.NodeMapper;
import cn.cislc.dockerservice.mapper.ServiceMapper;
import cn.cislc.dockerservice.param.ServiceParam;
import cn.cislc.dockerservice.service.NodeService;
import cn.cislc.dockerservice.service.ServiceService;
import cn.cislc.dockerservice.service.ThreadService;
import cn.cislc.dockerservice.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.SwarmNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author conghuhu
 * @since 2022-03-07
 */
@Slf4j
@Service
public class NodeServiceImpl extends ServiceImpl<NodeMapper, Node> implements NodeService {

    private final NodeMapper nodeMapper;

    private final ServiceMapper serviceMapper;


    private final ServiceService serviceService;

    private final ThreadService threadService;

    private final RedisUtil redisUtil;

    private static Map<String, List<ServiceParam>> currentRepairingServiceList = new ConcurrentHashMap<>();

    public NodeServiceImpl(NodeMapper nodeMapper, ServiceMapper serviceMapper, ServiceService serviceService, ThreadService threadService, RedisUtil redisUtil) {
        this.nodeMapper = nodeMapper;
        this.serviceMapper = serviceMapper;
        this.serviceService = serviceService;
        this.threadService = threadService;
        this.redisUtil = redisUtil;
    }

    @Override
    public List<Node> updateNodeList(List<SwarmNode> nodeList) {
        List<Node> resList = new ArrayList<>();
        nodeList.forEach(node -> {
            Integer count = nodeMapper.selectCount(new LambdaQueryWrapper<Node>().eq(Node::getId, node.getId()));
            Node newNode = initNode(node);
            resList.add(newNode);
            if (count > 0) {
                nodeMapper.updateById(newNode);
            } else {
                nodeMapper.insert(newNode);
            }
        });
        return resList;
    }

    @Override
    public void notifyEdgeCover(DockerClient dockerClient, String nodeId) throws IOException, InterruptedException, ExecutionException {
        // 1.删掉中心端的service，通知数据同步，告诉网关reject
        List<cn.cislc.dockerservice.entity.Service> serviceList = serviceMapper.selectList(new LambdaQueryWrapper<cn.cislc.dockerservice.entity.Service>()
                .eq(cn.cislc.dockerservice.entity.Service::getFromId, nodeId));
        currentRepairingServiceList.put(nodeId, new ArrayList<>());
        serviceList.forEach(item -> {
            try {
                String serviceId = item.getId();
                ServiceParam serviceParam = new ServiceParam();
                BeanUtils.copyProperties(item, serviceParam);
                serviceParam.setNetWork(item.getNetworkId());
                serviceParam.setServiceId(serviceId);
                Node node = nodeMapper.selectById(nodeId);
                serviceParam.setConstraints(Arrays.asList("node.labels.func == " + node.getLabel()));
                serviceParam.setFromId(null);
                serviceService.deleteService(dockerClient, serviceId);
                // 2.在nodeId结点上重启服务,启动完毕后告知网关
                cn.cislc.dockerservice.entity.Service service = serviceService.createService(dockerClient, serviceParam);
                if (service != null) {
                    log.info("在{}创建了：{}", node.getLabel(), service);
                    currentRepairingServiceList.get(nodeId).add(serviceParam);
                } else {
                    log.info("在{}创建{}失败了", node.getLabel(), item.getServiceName());
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 轮询检测currentRepairingServiceList中的nodeId对应的service是否全部重启
        Future<Boolean> future = threadService.waitForAllServiceRun(currentRepairingServiceList.get(nodeId));
        if (future.get()) {
            // 通知网关
            log.info("通知网关{}", future.get());
            redisUtil.del(nodeId);
        }

    }

    public Node initNode(SwarmNode node) {
        Node newNode = new Node();
        newNode.setId(node.getId());
        newNode.setIp(node.getStatus().getAddress());
        newNode.setHostname(node.getDescription().getHostname());
        newNode.setOs(node.getDescription().getPlatform().getOs());
        newNode.setArchitecture(node.getDescription().getPlatform().getArchitecture());
        newNode.setUpdateTime(LocalDateTime.ofInstant(node.getUpdatedAt().toInstant(), ZoneId.systemDefault()));
        newNode.setCreatedTime(LocalDateTime.ofInstant(node.getCreatedAt().toInstant(), ZoneId.systemDefault()));
        newNode.setRole(node.getSpec().getRole().name());
        newNode.setLabel(node.getSpec().getLabels().get("func"));
        return newNode;
    }
}
