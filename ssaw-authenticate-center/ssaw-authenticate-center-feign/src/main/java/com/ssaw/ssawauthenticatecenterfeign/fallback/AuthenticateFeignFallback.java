package com.ssaw.ssawauthenticatecenterfeign.fallback;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.vo.*;
import com.ssaw.ssawauthenticatecenterfeign.feign.AuthenticateFeign;
import com.ssaw.ssawauthenticatecenterfeign.vo.resource.UploadResourceVO;
import com.ssaw.ssawauthenticatecenterfeign.vo.scope.ScopeVO;
import com.ssaw.ssawauthenticatecenterfeign.vo.user.SimpleUserAttributeVO;
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
    public CommonResult<SimpleUserAttributeVO> authenticate(String requestUri) {
        return CommonResult.createResult(ERROR, "服务降级", null);
    }

    @Override
    public CommonResult<UploadResourceVO> uploadResource(UploadResourceVO uploadResourceVO) {
        return CommonResult.createResult(ERROR, "服务降级", uploadResourceVO);
    }

    @Override
    public CommonResult<String> uploadScopes(List<ScopeVO> scopeVOList) {
        return CommonResult.createResult(ERROR, "服务降级", "error");
    }

    @Override
    public CommonResult<String> uploadMenus(MenuVO menuVO, String resourceId) {
        return CommonResult.createResult(ERROR, "服务降级", "error");
    }

    @Override
    public CommonResult<String> uploadButtons(List<ButtonVO> buttonVOS, String resourceId) {
        return CommonResult.createResult(ERROR, "服务降级", "error");
    }

    @Override
    public CommonResult<String> uploadWhiteList(List<String> whiteList, String resourceId) {
        return CommonResult.createResult(ERROR, "服务降级", "error");
    }

    @Override
    public CommonResult<String> getApplicationName() {
        return CommonResult.createResult(ERROR, "服务降级", "error");
    }
}
