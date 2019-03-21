package com.ssaw.ssawauthenticatecenterfeign.feign;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.vo.*;
import com.ssaw.ssawauthenticatecenterfeign.fallback.AuthenticateFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;


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

    /**
     * 上传资源 白名单 作用域 菜单 按钮
     * @param uploadVO 上传信息数据模型
     * @return 上传结果
     */
    @PostMapping("/api/resource/uploadAuthenticateInfo")
    CommonResult<UploadVO> uploadAuthenticateInfo(@RequestBody UploadVO uploadVO);
}
