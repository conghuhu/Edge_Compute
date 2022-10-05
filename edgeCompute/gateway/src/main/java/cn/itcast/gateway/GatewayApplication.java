package cn.itcast.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author conghuhu
 * @create 2021-11-13 16:21
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "cn.itcast.feign.clients")
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
