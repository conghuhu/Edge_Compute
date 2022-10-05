package cn.itcast.gateway;

import cn.itcast.feign.clients.UserClient;
import cn.itcast.feign.pojo.TokenParam;
import cn.itcast.gateway.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 鉴权过滤器
 *
 * @author Administrator
 */
@Slf4j
@Order(1)
@Component
public class AuthorizeFilter implements GlobalFilter {

    @Autowired
    private UserClient userClient;

    private final JwtTokenUtil jwtTokenUtil;

    private static List<String> allowList = new ArrayList<>(Arrays.asList(
            "/auth/authentication/login",
            "/auth/admin/register",
            "/auth/authentication/checkToken"
    ));

    public AuthorizeFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String path = uri.getPath();
        for (int i = 0; i < allowList.size(); i++) {
            if (allowList.get(i).contains(path)) {
                return chain.filter(exchange);
            }
        }
        HttpHeaders requestHeaders = request.getHeaders();
        List<String> tokenList = requestHeaders.get("token");
        if (tokenList == null || tokenList.size() == 0) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        for (int i = 0; i < tokenList.size(); i++) {
            String token = tokenList.get(i);
            TokenParam tokenParam = new TokenParam();
            tokenParam.setToken(token);
            Boolean tokenValid = userClient.checkToken(tokenParam);
            if (tokenValid) {
                return chain.filter(exchange);
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
        return chain.filter(exchange);
    }
}
