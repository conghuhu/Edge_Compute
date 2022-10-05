package cn.itcast.feign.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author conghuhu
 * @create 2022-03-10 18:20
 */
@Data
public class AdminInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String avatar;

    private String email;

    private Boolean status;

    private Boolean admin;

    private String salt;
}
