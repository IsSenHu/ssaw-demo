package com.ssaw.uaa.filter;

import com.alibaba.fastjson.JSON;
import com.ssaw.ssawauthenticatecenterfeign.vo.user.UserInfoVO;
import com.ssaw.uaa.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;

/**
 * 所有的请求会经过此filter，然后对JWT Token进行解析校验，并转换成系统内部的Token，
 * 并把路由的服务名也加入header，送往接下来的路由服务里，方便进行鉴权
 * @author HuSen
 * @date 2019/4/27 14:27
 */
@Component
public class AuthFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        Route gatewayUrl = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        URI uri = gatewayUrl.getUri();
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(JwtUtil.HEADER_AUTH);
        UserInfoVO userInfoVO = JwtUtil.validateToken(token);
        ServerHttpRequest.Builder mutate = request.mutate();
        if (Objects.nonNull(userInfoVO)) {
            mutate.header("userInfo", JSON.toJSONString(userInfoVO));
        } else {
            throw new RuntimeException("user not exist, please check");
        }
        ServerHttpRequest buildRequest = mutate.build();
        return chain.filter(exchange.mutate().request(buildRequest).build());
    }
}