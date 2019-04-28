package com.ssaw.uaa.filter;

import com.alibaba.fastjson.JSON;
import com.ssaw.commons.util.json.jack.JsonUtils;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.feign.AuthenticateFeign;
import com.ssaw.ssawauthenticatecenterfeign.vo.user.UserInfoVO;
import com.ssaw.uaa.store.TokenStore;
import com.ssaw.uaa.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

import static com.ssaw.commons.constant.Constants.ResultCodes.SUCCESS;

/**
 * 1.
 * 所有的请求会经过此filter，然后对JWT Token进行解析校验，并转换成系统内部的Token
 * @author HuSen
 * @date 2019/4/27 14:27
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter {

    @Resource
    private AuthenticateFeign authenticateFeign;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Route gatewayUrl = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(JwtUtil.HEADER_AUTH);
        TokenStore.store(token);
        ServerHttpRequest.Builder mutate = request.mutate();
        if (!StringUtils.startsWith(token, JwtUtil.BEARER)) {
            UserInfoVO userInfoVO = JwtUtil.validateToken(token);
            if (Objects.isNull(userInfoVO)) {
                throw new RuntimeException("user not exist, please check");
            }
            try {
                mutate.header("userInfo", URLEncoder.encode(JSON.toJSONString(userInfoVO), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } else {
            String value = request.getPath().value();
            CommonResult<String> authenticate = authenticateFeign.authenticate(value);
            log.info("OAUTH2 认证结果:{}", JsonUtils.object2JsonString(authenticate));
            if (authenticate.getCode() != SUCCESS) {
                throw new RuntimeException("is not permit to access");
            }
            try {
                mutate.header("userInfo", URLEncoder.encode(authenticate.getData(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        ServerHttpRequest buildRequest = mutate.build();
        TokenStore.clear();
        return chain.filter(exchange.mutate().request(buildRequest).build());
    }
}