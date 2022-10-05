package cn.cislc.dockerservice.service;

import cn.cislc.dockerservice.entity.Service;
import cn.cislc.dockerservice.param.ServiceParam;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.dockerjava.api.DockerClient;

import java.io.IOException;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author conghuhu
 * @since 2022-03-07
 */
public interface ServiceService extends IService<Service> {

    Service createService(DockerClient dockerClient, ServiceParam serviceParam) throws IOException, InterruptedException;

    void updateService(com.github.dockerjava.api.model.Service service);

    void deleteService(DockerClient dockerClient, String serviceId) throws IOException;
}
