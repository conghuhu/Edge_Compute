package cn.cislc.dockerservice.controller;

import cn.cislc.cloudcommon.result.JsonResult;
import cn.cislc.cloudcommon.result.ResultTool;
import cn.cislc.dockerservice.entity.Container;
import cn.cislc.dockerservice.service.ContainerService;
import cn.cislc.dockerservice.vo.ContainerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author conghuhu
 * @create 2022-03-09 10:02
 */
@Api(tags = "容器")
@Slf4j
@RestController
@RequestMapping("/container")
public class ContainerController {

    private final ContainerService containerService;

    public ContainerController(ContainerService containerService) {
        this.containerService = containerService;
    }

    @ApiOperation(value = "根据nodeId获取其容器列表", notes = "根据nodeId获取其容器列表", produces = "application/json")
    @GetMapping("/getContainerListByNodeId/{nodeId}")
    public JsonResult getContainerListByNodeId(@PathVariable String nodeId) {
        List<ContainerVo> containerVoList = containerService.getContainerList(nodeId);
        return ResultTool.success(containerVoList);
    }
}
