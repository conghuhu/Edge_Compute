package cn.itcast.feign.clients;

import cn.itcast.feign.clients.fallback.UserClientFallbackFactory;
import cn.itcast.feign.pojo.TokenParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author conghuhu
 * @create 2021-11-12 19:37
 */
@Component
@FeignClient(value = "authservice", fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {

    @PostMapping("/auth/authentication/checkToken")
    Boolean checkToken(@RequestBody TokenParam tokenParam);
}
