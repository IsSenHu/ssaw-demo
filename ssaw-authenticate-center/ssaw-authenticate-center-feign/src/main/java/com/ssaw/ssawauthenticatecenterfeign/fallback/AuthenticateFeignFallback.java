package com.ssaw.ssawauthenticatecenterfeign.fallback;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.dto.Button;
import com.ssaw.ssawauthenticatecenterfeign.dto.Menu;
import com.ssaw.ssawauthenticatecenterfeign.dto.ResourceDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.ScopeDto;
import com.ssaw.ssawauthenticatecenterfeign.feign.AuthenticateFeign;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ssaw.commons.constant.Constants.ResultCodes.ERROR;

/**
 * @author HuSen
 * @date 2019/02/14
 */
@Component
public class AuthenticateFeignFallback implements AuthenticateFeign {

    @Override
    public CommonResult<String> authenticate(String requestUri) {
        return CommonResult.createResult(ERROR, "服务降级", requestUri);
    }

    @Override
    public CommonResult<ResourceDto> uploadResource(ResourceDto resourceDto) {
        return CommonResult.createResult(ERROR, "服务降级", resourceDto);
    }

    @Override
    public CommonResult<String> uploadScopes(List<ScopeDto> scopeDtoList) {
        return CommonResult.createResult(ERROR, "服务降级", "error");
    }

    @Override
    public CommonResult<String> uploadMenus(Menu menu, String resourceId) {
        return CommonResult.createResult(ERROR, "服务降级", "error");
    }

    @Override
    public CommonResult<String> uploadButtons(List<Button> buttons, String resourceId) {
        return CommonResult.createResult(ERROR, "服务降级", "error");
    }

    @Override
    public CommonResult<String> uploadWhiteList(List<String> whiteList, String resourceId) {
        return CommonResult.createResult(ERROR, "服务降级", "error");
    }
}
