package cn.cislc.dockerservice.job;

import cn.cislc.dockerservice.entity.Container;
import cn.cislc.dockerservice.entity.Service;
import cn.cislc.dockerservice.mapper.ContainerMapper;
import cn.cislc.dockerservice.param.ServiceParam;
import cn.cislc.dockerservice.service.ServiceService;
import cn.cislc.dockerservice.service.ThreadService;
import cn.cislc.dockerservice.utils.DockerUtils;
import cn.cislc.dockerservice.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.SwarmNode;

import com.github.dockerjava.api.model.Task;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author conghuhu
 * @create 2022-03-06 15:43
 */
@Component
@Slf4j
public class DockerHealthJob {

    private static final String centerNode = "center-node";

    private final DockerUtils dockerUtils;

    private final RedisUtil redisUtil;

    private final ServiceService serviceService;

    private final ContainerMapper containerMapper;

    private final ThreadService threadService;

    @Value("${swam.managerUrl}")
    private String tcpUrl;

    private DockerClient dockerClient;

    private static Map<String, List<ServiceParam>> currentRepairingServiceList = new ConcurrentHashMap<>();


    public DockerHealthJob(DockerUtils dockerUtils, RedisUtil redisUtil, ServiceService serviceService, ContainerMapper containerMapper, ThreadService threadService) {
        this.dockerUtils = dockerUtils;
        this.redisUtil = redisUtil;
        this.serviceService = serviceService;
        this.containerMapper = containerMapper;
        this.threadService = threadService;
    }

    /**
     * 服务器或docker宕机：node节点status为down
     *
     * @throws InterruptedException
     */
    @SneakyThrows
    public void executeInternal() {
        DockerClient dockerClient = dockerUtils.connectDocker(tcpUrl);
        this.dockerClient = dockerClient;
        List<SwarmNode> nodeList = dockerUtils.getNodeList(dockerClient);

        nodeList.stream().filter(item -> "down".equals(item.getStatus().getState().name())
                        || "DOWN".equals(item.getStatus().getState().name()))
                .forEach(item -> {
                    // docker异常的服务器，需要通知重启该服务器下的docker，并通知数据同步模块和网关
                    String nodeId = item.getId();
                    String status = (String) redisUtil.get(nodeId);
                    if (!StringUtils.hasText(status)) {
                        redisUtil.set(nodeId, "down");
                        // 记录下挂的结点
                        currentRepairingServiceList.put(nodeId, new ArrayList<>());
                        try {
                            decisionEngine(dockerClient, item);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });
        dockerClient.close();
    }

    public void decisionEngine(DockerClient dockerClient, SwarmNode node) throws IOException, InterruptedException, ExecutionException {
        String hostname = node.getDescription().getHostname();
        log.info("开始检测");
        if (centerNode.equals(hostname)) {
            // 中心端挂了，联系管理员，通知网关
            log.info("中心结点挂了");
        } else {
            // 在中心端重启该node下所有的镜像
            String nodeLabel = node.getSpec().getLabels().get("func");
            List<Task> taskList = dockerUtils.getTaskList(dockerClient);
            String nodeId = node.getId();
            taskList.stream()
                    .filter(item -> item.getNodeId() == null &&
                            nodeLabel.equals(item.getSpec().getPlacement().getConstraints().get(0).split("==")[1].trim()))
                    .forEach(task -> {
                        String serviceId = task.getServiceId();
                        ServiceParam serviceParam = new ServiceParam();
                        Service serviceFromDataBase = serviceService.getById(serviceId);
                        Container container = containerMapper.selectOne(new LambdaQueryWrapper<Container>()
                                .eq(Container::getServiceId, serviceId));
                        serviceParam.setServiceName(serviceFromDataBase.getServiceName());
                        serviceParam.setConstraints(Arrays.asList("node.labels.func == center"));
                        serviceParam.setImage(container.getImage());
                        serviceParam.setPublishedPort(container.getPublishedPort());
                        serviceParam.setTargetPort(container.getTargetPort());
                        serviceParam.setNetWork(container.getNetworkId());
                        serviceParam.setFromId(node.getId());
                        try {
                            serviceService.deleteService(dockerClient, serviceId);
                            Service service = serviceService.createService(dockerClient, serviceParam);
                            if (service != null) {
                                serviceParam.setServiceId(service.getId());
                                log.info("在center创建了：{}", service);
                                // 将挂的service放入currentRepairingServiceList中。
                                currentRepairingServiceList.get(nodeId).add(serviceParam);
                            } else {
                                log.info("在center创建{}失败了", serviceFromDataBase.getServiceName());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
            // 轮询检测currentRepairingServiceList中的nodeId对应的service是否全部重启
            Future<Boolean> future = threadService.waitForAllServiceRun(currentRepairingServiceList.get(nodeId));
            if (future.get()) {
                // 通知网关
                log.info("通知网关{}", future.get());

            }
        }
    }

}
