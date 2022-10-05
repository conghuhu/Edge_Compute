package cn.cislc.dockerservice.param;

import lombok.Data;

import java.util.List;

/**
 * @author conghuhu
 * @create 2022-03-06 15:25
 */
@Data
public class ServiceParam {

    private String serviceId;

    private String serviceName;

    private String image;

    private Integer publishedPort;

    private Integer targetPort;

    private String netWork;

    private List<String> constraints;

    private String fromId;
}
