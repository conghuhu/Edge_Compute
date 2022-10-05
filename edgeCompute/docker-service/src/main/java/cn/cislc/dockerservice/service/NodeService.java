package cn.cislc.dockerservice.service;

import cn.cislc.dockerservice.entity.Node;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.SwarmNode;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author conghuhu
 * @since 2022-03-07
 */
public interface NodeService extends IService<Node> {

    List<Node> updateNodeList(List<SwarmNode> nodeList);

    void notifyEdgeCover(DockerClient dockerClient, String nodeId) throws IOException, InterruptedException, ExecutionException;

}
