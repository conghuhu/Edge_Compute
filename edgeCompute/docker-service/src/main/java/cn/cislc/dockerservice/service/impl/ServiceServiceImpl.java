package cn.cislc.dockerservice.service.impl;

import cn.cislc.dockerservice.entity.Container;
import cn.cislc.dockerservice.entity.Service;
import cn.cislc.dockerservice.mapper.ServiceMapper;
import cn.cislc.dockerservice.param.ServiceParam;
import cn.cislc.dockerservice.service.ContainerService;
import cn.cislc.dockerservice.service.ServiceService;
import cn.cislc.dockerservice.service.ThreadService;
import cn.cislc.dockerservice.utils.DockerUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateServiceResponse;
import com.github.dockerjava.api.model.SwarmNode;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author conghuhu
 * @since 2022-03-07
 */
@org.springframework.stereotype.Service
public class ServiceServiceImpl extends ServiceImpl<ServiceMapper, Service> implements ServiceService {

    private final ServiceMapper serviceMapper;

    private final DockerUtils dockerUtils;

    private final ThreadService threadService;

    private final ContainerService containerService;

    public ServiceServiceImpl(ServiceMapper serviceMapper, DockerUtils dockerUtils, ThreadService threadService, ContainerService containerService) {
        this.serviceMapper = serviceMapper;
        this.dockerUtils = dockerUtils;
        this.threadService = threadService;
        this.containerService = containerService;
    }

    @Override
    public Service createService(DockerClient dockerClient, ServiceParam serviceParam) throws IOException, InterruptedException {

        String label = serviceParam.getConstraints().get(0).split("==")[1].trim();

        List<SwarmNode> nodeList = dockerUtils.getNodeList(dockerClient);
        AtomicReference<Boolean> currentNodeRunning = new AtomicReference<>(true);
        nodeList.stream().filter(item -> label.equals(item.getSpec().getLabels().get("func")))
                .forEach(item -> {
                    String status = item.getStatus().getState().name().toLowerCase(Locale.ROOT);
                    if ("down".equals(status)) {
                        currentNodeRunning.set(false);
                    }
                });
        if (currentNodeRunning.get()) {
            CreateServiceResponse response = dockerUtils.createService(dockerClient, serviceParam.getServiceName(), serviceParam.getImage(),
                    serviceParam.getPublishedPort(), serviceParam.getTargetPort(), serviceParam.getNetWork(), serviceParam.getConstraints());
            serviceParam.setServiceId(response.getId());
            com.github.dockerjava.api.model.Service inspectService = dockerUtils.inspectService(dockerClient, response.getId());

            Service service = initService(serviceParam);
            serviceMapper.insert(service);

            this.updateService(inspectService);
            threadService.pollingWaitContainerBuild(inspectService.getSpec().getTaskTemplate().getContainerSpec().getImage(),
                    response.getId());

            return service;
        } else {
            return null;
        }
    }

    @Override
    public void updateService(com.github.dockerjava.api.model.Service service) {
        Service updateService = new Service();
        updateService.setId(service.getId());
        updateService.setImage(service.getSpec().getTaskTemplate().getContainerSpec().getImage());
        updateService.setServiceName(service.getSpec().getName());
        updateService.setCreateTime(LocalDateTime.ofInstant(service.getCreatedAt().toInstant(), ZoneId.systemDefault()));
        updateService.setTargetPort(service.getEndpoint().getPorts()[0].getTargetPort());
        updateService.setPublishedPort(service.getEndpoint().getPorts()[0].getPublishedPort());
        updateService.setReplicas(Integer.valueOf(String.valueOf(service.getSpec().getMode().getReplicated().getReplicas())));
        updateService.setRuntime(service.getSpec().getTaskTemplate().getRuntime());
        List<String> constraints = service.getSpec().getTaskTemplate().getPlacement().getConstraints();
        String constraintString = String.join(";", constraints);
        updateService.setConstraints(constraintString);
        updateService.setNetworkId(service.getSpec().getNetworks().get(0).getTarget());
        updateService.setPublishMode(service.getSpec().getEndpointSpec().getPorts().get(0).getPublishMode().name());
        serviceMapper.updateById(updateService);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteService(DockerClient dockerClient, String serviceId) throws IOException {
        dockerUtils.deleteService(dockerClient, serviceId);
        serviceMapper.deleteById(serviceId);
        containerService.remove(new LambdaQueryWrapper<Container>().eq(Container::getServiceId, serviceId));
    }

    public Service initService(ServiceParam serviceParam) {
        Service service = new Service();
        BeanUtils.copyProperties(serviceParam, service);
        service.setId(serviceParam.getServiceId());
        List<String> constraints = serviceParam.getConstraints();
        String constraintString = String.join(";", constraints);
        service.setConstraints(constraintString);
        return service;
    }

}
