package cn.itcast.feign.clients.fallback;

import cn.itcast.feign.clients.UserClient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable throwable) {
        return token -> false;
    }
}