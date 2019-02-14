package com.ssaw.ssawauthenticatecenterfeign.feign;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.fallback.AuthenticateFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author HuSen
 * @date 2019/02/14
 */
@Component
@FeignClient(name = "ssaw-authenticate-center", fallback = AuthenticateFeignFallback.class)
public interface AuthenticateFeign {

    /**
     * 用户认证
     * @param requestUri 请求uri
     * @return 认证结果
     */
    @GetMapping("/api/authenticate")
    CommonResult<String> authenticate(@RequestParam(name = "requestUri") String requestUri);
}
