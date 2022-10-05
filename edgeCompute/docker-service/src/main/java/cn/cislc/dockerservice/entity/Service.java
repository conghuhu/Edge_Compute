package cn.cislc.dockerservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
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
@TableName("m_service")
@ApiModel(value = "Service对象", description = "")
public class Service implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "名称")
    private String serviceName;

    @ApiModelProperty(value = "容器内端口")
    private Integer targetPort;

    @ApiModelProperty(value = "外部端口")
    private Integer publishedPort;

    @ApiModelProperty(value = "镜像")
    private String image;

    @ApiModelProperty(value = "限制")
    private String constraints;

    @ApiModelProperty(value = "比如container")
    private String runtime;

    private String publishMode;

    @ApiModelProperty(value = "副本数")
    private Integer replicas;


    @ApiModelProperty(value = "network的id")
    private String networkId;

    @ApiModelProperty(value = "容器")
    private String isolation;

    private String fromId;
}
