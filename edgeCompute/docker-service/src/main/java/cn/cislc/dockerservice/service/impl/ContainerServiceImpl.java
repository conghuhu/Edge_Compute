package cn.cislc.dockerservice.service.impl;

import cn.cislc.dockerservice.entity.Container;
import cn.cislc.dockerservice.entity.Network;
import cn.cislc.dockerservice.mapper.ContainerMapper;
import cn.cislc.dockerservice.mapper.NetworkMapper;
import cn.cislc.dockerservice.mapper.ServiceMapper;
import cn.cislc.dockerservice.service.ContainerService;
import cn.cislc.dockerservice.vo.ContainerVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.dockerjava.api.model.Task;
import com.github.dockerjava.api.model.TaskStatusContainerStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
public class ContainerServiceImpl extends ServiceImpl<ContainerMapper, Container> implements ContainerService {

    private final ContainerMapper containerMapper;

    private final ServiceMapper serviceMapper;

    private final NetworkMapper networkMapper;

    public ContainerServiceImpl(ContainerMapper containerMapper, ServiceMapper serviceMapper, NetworkMapper networkMapper) {
        this.containerMapper = containerMapper;
        this.serviceMapper = serviceMapper;
        this.networkMapper = networkMapper;
    }


    @Override
    public void refresh(List<Task> taskList) {
        taskList.stream().filter(item -> !"shutdown".equals(item.getDesiredState().getValue())
                        && !"remove".equals(item.getDesiredState().getValue())
                )
                .forEach(task -> {
                    log.info("{}", task);
                    Integer count = containerMapper.selectCount(
                            new LambdaQueryWrapper<Container>()
                                    .eq(Container::getImage, task.getSpec().getContainerSpec().getImage())
                                    .eq(Container::getTaskId, task.getId())
                    );
                    TaskStatusContainerStatus containerStatus = task.getStatus().getContainerStatus();
                    if (containerStatus != null) {
                        cn.cislc.dockerservice.entity.Service service = serviceMapper.selectById(task.getServiceId());
                        Container container = initContainer(containerStatus.getContainerID(),
                                task.getSpec().getContainerSpec().getImage(), task.getServiceId(), task.getId()
                                , task.getNodeId(), service.getTargetPort(), service.getPublishedPort(), service.getNetworkId()
                        );
                        if (count > 0) {
                            containerMapper.updateById(container);
                        } else {
                            containerMapper.insert(container);
                        }
                    }
                });
    }

    @Override
    public void createContainer(String containerID, String image,
                                String serviceId, String taskId, String nodeId,
                                Integer targetPort, Integer publishedPort, String networkId) {
        Container container = initContainer(containerID, image, serviceId, taskId, nodeId, targetPort, publishedPort, networkId);
        containerMapper.insert(container);
    }

    @Override
    public List<ContainerVo> getContainerList(String nodeId) {
        List<Container> containerList = containerMapper.selectList(new LambdaQueryWrapper<Container>()
                .eq(Container::getNodeId, nodeId));
        List<ContainerVo> resList = new ArrayList<>();
        containerList.parallelStream().forEach(container -> {
            ContainerVo containerVo = new ContainerVo();
            BeanUtils.copyProperties(container, containerVo);
            cn.cislc.dockerservice.entity.Service service = serviceMapper.selectById(container.getServiceId());
            containerVo.setService(service);
            Network network = networkMapper.selectById(container.getNetworkId());
            containerVo.setNetwork(network);
            resList.add(containerVo);
        });
        return resList;
    }

    public Container initContainer(String containerID, String image,
                                   String serviceId, String taskId, String nodeId,
                                   Integer targetPort, Integer publishedPort, String networkId) {
        Container container = new Container();
        container.setId(containerID);
        container.setImage(image);
        container.setServiceId(serviceId);
        container.setTaskId(taskId);
        container.setNodeId(nodeId);
        container.setTargetPort(targetPort);
        container.setPublishedPort(publishedPort);
        container.setNetworkId(networkId);
        return container;

    }
}
