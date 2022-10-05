package cn.cislc.dockerservice.service;

import cn.cislc.dockerservice.mapper.ServiceMapper;
import cn.cislc.dockerservice.param.ServiceParam;
import cn.cislc.dockerservice.utils.DockerUtils;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.SwarmNode;
import com.github.dockerjava.api.model.Task;
import com.github.dockerjava.api.model.TaskStatusContainerStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author conghuhu
 * @create 2022-03-05 17:55
 */
@Slf4j
@Service
public class ThreadService {

    private final DockerUtils dockerUtils;

    private final ContainerService containerService;

    private final ServiceMapper serviceMapper;

    @Value("${swam.managerUrl}")
    private String tcpUrl;

    public ThreadService(DockerUtils dockerUtils, ContainerService containerService, ServiceMapper serviceMapper) {
        this.dockerUtils = dockerUtils;
        this.containerService = containerService;
        this.serviceMapper = serviceMapper;
    }

    @Async
    public void pollingWaitContainerBuild(String image, String serviceId) throws IOException, InterruptedException {
        DockerClient dockerClient = dockerUtils.connectDocker(tcpUrl);
        AtomicBoolean complete = new AtomicBoolean(false);
        log.info("开始循环检查{}服务,镜像为{},其容器是否在子节点成功运行", serviceId, image);
        while (!complete.get()) {
            Thread.sleep(500);
            List<Task> taskList = dockerUtils.getTaskList(dockerClient);
            taskList.stream().filter(item -> serviceId.equals(item.getServiceId())
                            && image.equals(item.getSpec().getContainerSpec().getImage())
                            && !"shutdown".equals(item.getDesiredState().getValue()))
                    .forEach(task -> {
                        TaskStatusContainerStatus containerStatus = task.getStatus().getContainerStatus();
                        if (containerStatus != null) {
                            String containerID = containerStatus.getContainerID();
                            String nodeId = task.getNodeId();
                            String taskId = task.getId();
                            cn.cislc.dockerservice.entity.Service service = serviceMapper.selectById(serviceId);
                            containerService.createContainer(containerID, image, serviceId, taskId,
                                    nodeId, service.getTargetPort(), service.getPublishedPort(), service.getNetworkId());
                            complete.set(true);
                            log.info("{}服务,镜像为{},其容器在子节点成功运行", serviceId, image);
                        }
                    });
        }
        dockerClient.close();
    }

    @Async
    public Future<Boolean> waitForAllServiceRun(List<ServiceParam> serviceParamList) throws IOException, InterruptedException {
        log.info("开始等待挂的结点全部在中心端启动");
        DockerClient dockerClient = dockerUtils.connectDocker(tcpUrl);
        while (serviceParamList.size() > 0) {
            Thread.sleep(500);
            List<Task> taskList = dockerUtils.getTaskList(dockerClient);
            List<Integer> deleteList = new ArrayList<>();
            AtomicInteger index = new AtomicInteger();
            serviceParamList.forEach(item -> {
                taskList.stream().filter(task -> item.getServiceId().equals(task.getServiceId())).forEach(task -> {
                    TaskStatusContainerStatus containerStatus = task.getStatus().getContainerStatus();
                    if (containerStatus != null && containerStatus.getContainerID() != null) {
                        deleteList.add(index.intValue());
                    }
                });
                index.getAndIncrement();
            });
            deleteList.forEach(item -> {
                serviceParamList.remove(item.intValue());
            });
        }
        dockerClient.close();
        return AsyncResult.forValue(true);
    }
}
