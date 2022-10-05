package cn.cislc.dockerservice.controller;

import cn.cislc.cloudcommon.result.JsonResult;
import cn.cislc.cloudcommon.result.ResultTool;
import cn.cislc.dockerservice.entity.Node;
import cn.cislc.dockerservice.service.NodeService;
import cn.cislc.dockerservice.utils.DockerUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateServiceResponse;
import com.github.dockerjava.api.model.SwarmNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.HttpHostConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author conghuhu
 * @create 2022-03-04 16:08
 */
@Api(tags = "Node")
@Slf4j
@RestController
@RequestMapping("/node")
public class NodeController {

    private final DockerUtils dockerUtils;

    private final NodeService nodeService;

    public NodeController(DockerUtils dockerUtils, NodeService nodeService) {
        this.dockerUtils = dockerUtils;
        this.nodeService = nodeService;
    }

    @Value("${swam.managerUrl}")
    private String tcpUrl;

    @ApiOperation(value = "获取node列表", notes = "获取node列表", produces = "application/json")
    @GetMapping("/getNodeList")
    public JsonResult<List<Node>> getNodeList() throws IOException {
        DockerClient dockerClient = dockerUtils.connectDocker(tcpUrl);
        List<SwarmNode> nodeList = dockerUtils.getNodeList(dockerClient);
        dockerClient.close();
        List<Node> resList = nodeService.updateNodeList(nodeList);
        return ResultTool.success(resList);
    }

    @ApiOperation(value = "恢复某个结点下原有的容器", notes = "恢复某个结点下原有的容器", produces = "application/json")
    @PostMapping("/notifyEdgeCover/{nodeId}")
    public JsonResult notifyEdgeCover(@PathVariable String nodeId) throws IOException, ExecutionException, InterruptedException {
        DockerClient dockerClient = dockerUtils.connectDocker(tcpUrl);
        nodeService.notifyEdgeCover(dockerClient, nodeId);
        dockerClient.close();
        return ResultTool.success("后台已启动，请1~2分钟后查看");
    }
}
