package cn.cislc.dockerservice.vo;

import cn.cislc.dockerservice.entity.Network;
import cn.cislc.dockerservice.entity.Service;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author conghuhu
 * @create 2022-03-09 10:41
 */
@ApiModel(value = "ContainerVo对象", description = "")
@Data
public class ContainerVo {
    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "镜像")
    private String image;

    @ApiModelProperty(value = "容器所在节点的id")
    private String nodeId;

    private Service service;

    private String taskId;

    @ApiModelProperty(value = "容器内端口")
    private Integer targetPort;

    @ApiModelProperty(value = "外部端口")
    private Integer publishedPort;

    private Network network;
}
