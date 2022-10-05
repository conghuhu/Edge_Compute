package cn.cislc.dockerservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author conghuhu
 * @since 2022-03-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("m_container")
@ApiModel(value = "Container对象", description = "")
public class Container implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "镜像")
    private String image;

    @ApiModelProperty(value = "容器所在节点的id")
    private String nodeId;

    private String serviceId;

    private String taskId;

    @ApiModelProperty(value = "容器内端口")
    private Integer targetPort;

    @ApiModelProperty(value = "外部端口")
    private Integer publishedPort;

    private String networkId;
}
