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
@TableName("m_node")
@ApiModel(value = "Node对象", description = "")
public class Node implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "主机名")
    private String hostname;

    @ApiModelProperty(value = "操作系统")
    private String os;

    @ApiModelProperty(value = "系统结构")
    private String architecture;

    private LocalDateTime createdTime;

    private LocalDateTime updateTime;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "manager worker")
    private String role;

    private String label;
}
