package cn.cislc.dockerservice.service;

import cn.cislc.dockerservice.entity.Container;
import cn.cislc.dockerservice.vo.ContainerVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.dockerjava.api.model.Task;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author conghuhu
 * @since 2022-03-07
 */
public interface ContainerService extends IService<Container> {

    void refresh(List<Task> taskList);

    void createContainer(String containerID, String image, String serviceId, String taskId,
                         String nodeId, Integer targetPort, Integer publishedPort, String networkId);

    List<ContainerVo> getContainerList(String nodeId);
}
