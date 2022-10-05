package cn.cislc.dockerservice.controller;

import cn.cislc.cloudcommon.result.JsonResult;

import cn.cislc.cloudcommon.result.ResultTool;
import cn.cislc.dockerservice.entity.Container;
import cn.cislc.dockerservice.param.ServiceParam;
import cn.cislc.dockerservice.service.ContainerService;
import cn.cislc.dockerservice.service.ServiceService;
import cn.cislc.dockerservice.utils.DockerUtils;
import com.github.dockerjava.api.DockerClient;

import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.api.model.Task;
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
@Api(tags = "Service")
@Slf4j
@RestController
@RequestMapping("/service")
public class ServiceController {

    private final DockerUtils dockerUtils;

    private final ServiceService serviceService;

    private final ContainerService containerService;


    @Value("${swam.managerUrl}")
    private String tcpUrl;

    public ServiceController(DockerUtils dockerUtils, ServiceService serviceService, ContainerService containerService) {
        this.dockerUtils = dockerUtils;
        this.serviceService = serviceService;
        this.containerService = containerService;
    }

    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "创建service", notes = "创建service", produces = "application/json")
    @PostMapping("/createService")
    public JsonResult<cn.cislc.dockerservice.entity.Service> createService(@RequestBody ServiceParam serviceParam) throws IOException, InterruptedException {
        DockerClient dockerClient = dockerUtils.connectDocker(tcpUrl);
        cn.cislc.dockerservice.entity.Service service = serviceService.createService(dockerClient, serviceParam);
        dockerClient.close();
        if (service != null) {
            return ResultTool.success(service);
        } else {
            String label = serviceParam.getConstraints().get(0).split("==")[1].trim();
            return ResultTool.fail(label + "节点挂了，请迅速修复");
        }
    }

    @ApiOperation(value = "查看service列表", notes = "查看service列表", produces = "application/json")
    @GetMapping("/getServiceList")
    public JsonResult<List<Service>> getServiceList() throws IOException {
        DockerClient dockerClient = dockerUtils.connectDocker(tcpUrl);
        List<Service> serviceList = dockerUtils.getServiceListInfo(dockerClient);
        dockerClient.close();
        return ResultTool.success(serviceList);
    }

    @ApiOperation(value = "查看某个service", notes = "查看某个service", produces = "application/json")
    @GetMapping("/getInspectServiceInfo")
    public JsonResult<Service> getInspectServiceInfo(@RequestParam("serviceId") String serviceId) throws IOException {
        DockerClient dockerClient = dockerUtils.connectDocker(tcpUrl);
        Service service = dockerUtils.inspectService(dockerClient, serviceId);
        dockerClient.close();
        return ResultTool.success(service);
    }

    @ApiOperation(value = "删除某个service", notes = "删除某个service", produces = "application/json")
    @DeleteMapping("/deleteService")
    public JsonResult deleteService(@RequestParam("serviceId") String serviceId) throws IOException {
        DockerClient dockerClient = dockerUtils.connectDocker(tcpUrl);
        serviceService.deleteService(dockerClient, serviceId);
        dockerClient.close();
        return ResultTool.success();
    }

    @ApiOperation(value = "获取任务列表", notes = "获取任务列表", produces = "application/json")
    @GetMapping("/getTaskList")
    public JsonResult getTaskList() throws IOException {
        DockerClient dockerClient = dockerUtils.connectDocker(tcpUrl);
        List<Task> taskList = dockerUtils.getTaskList(dockerClient);
        containerService.refresh(taskList);
        dockerClient.close();
        return ResultTool.success(taskList);
    }
}
