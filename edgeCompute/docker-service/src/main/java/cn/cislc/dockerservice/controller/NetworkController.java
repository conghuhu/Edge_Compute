package cn.cislc.dockerservice.controller;

import cn.cislc.cloudcommon.result.JsonResult;
import cn.cislc.cloudcommon.result.ResultTool;
import cn.cislc.dockerservice.param.NetWorkParam;
import cn.cislc.dockerservice.service.NetworkService;
import cn.cislc.dockerservice.utils.DockerUtils;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.model.Network;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author conghuhu
 * @create 2022-03-05 16:32
 */
@Api(tags = "netWork")
@Slf4j
@RestController
@RequestMapping("/network")
public class NetworkController {

    private final NetworkService networkService;

    private final DockerUtils dockerUtils;

    @Value("${swam.managerUrl}")
    private String tcpUrl;

    public NetworkController(NetworkService networkService, DockerUtils dockerUtils) {
        this.networkService = networkService;
        this.dockerUtils = dockerUtils;
    }

    @ApiOperation(value = "获取network列表", notes = "获取network列表", produces = "application/json")
    @GetMapping("/getNetworkList")
    public JsonResult<List<cn.cislc.dockerservice.entity.Network>> getNetworkList() throws IOException {
        DockerClient dockerClient = dockerUtils.connectDocker(tcpUrl);
        List<Network> networkList = dockerUtils.getNetworkList(dockerClient);
        dockerClient.close();
        List<cn.cislc.dockerservice.entity.Network> resList = networkService.updateNetWorkList(networkList);
        return ResultTool.success(resList);
    }

    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "创建netWork", notes = "创建netWork", produces = "application/json")
    @PostMapping("/createNetwork")
    public JsonResult createNetwork(@RequestBody NetWorkParam netWorkParam) throws IOException {
        DockerClient dockerClient = dockerUtils.connectDocker(tcpUrl);
        CreateNetworkResponse networkResponse = dockerUtils.createNetwork(dockerClient,
                netWorkParam.getNetWorkName(), netWorkParam.getDrive(), netWorkParam.getCheckDuplicate());
        netWorkParam.setId(networkResponse.getId());
        networkService.createNetWork(netWorkParam);
        dockerClient.close();
        return ResultTool.success(networkResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "删除netWork", notes = "删除netWork", produces = "application/json")
    @DeleteMapping("/deleteNetwork")
    public JsonResult deleteNetwork(@RequestParam("networkId") String networkId) throws IOException {
        DockerClient dockerClient = dockerUtils.connectDocker(tcpUrl);
        dockerUtils.deleteNetwork(dockerClient, networkId);
        networkService.removeNetWork(networkId);
        dockerClient.close();
        return ResultTool.success();
    }
}
