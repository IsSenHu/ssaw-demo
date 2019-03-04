package com.ssaw.ssawauthenticatecenterfeign.feign;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.vo.*;
import com.ssaw.ssawauthenticatecenterfeign.fallback.AuthenticateFeignFallback;
import com.ssaw.ssawauthenticatecenterfeign.vo.resource.ResourceVO;
import com.ssaw.ssawauthenticatecenterfeign.vo.resource.UploadResourceVO;
import com.ssaw.ssawauthenticatecenterfeign.vo.scope.ScopeVO;
import com.ssaw.ssawauthenticatecenterfeign.vo.user.SimpleUserAttributeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    CommonResult<SimpleUserAttributeVO> authenticate(@RequestParam(name = "requestUri") String requestUri);

    /**
     * 新增一个资源
     * @param uploadResourceVO 资源数据模型
     * @return 操作结果
     */
    @PostMapping("/api/resource/uploadResource")
    CommonResult<UploadResourceVO> uploadResource(@RequestBody UploadResourceVO uploadResourceVO);

    /**
     * 批量上传Scope
     * @param scopeVOList ScopeDtoList
     * @return 结果
     */
    @PostMapping("/api/scope/uploadScopes")
    CommonResult<String> uploadScopes(@RequestBody List<ScopeVO> scopeVOList);

    /**
     * 上传菜单
     * @param menuVO 菜单
     * @param resourceId 资源ID
     * @return 上传结果
     */
    @PostMapping("/uploadMenus/{resourceId}")
    CommonResult<String> uploadMenus(@RequestBody MenuVO menuVO, @PathVariable(name = "resourceId") String resourceId);

    /**
     * 上传按钮
     * @param buttonVOS 按钮
     * @param resourceId 资源ID
     * @return 上传结果
     */
    @PostMapping("/uploadButtons/{resourceId}")
    CommonResult<String> uploadButtons(@RequestBody List<ButtonVO> buttonVOS, @PathVariable(name = "resourceId") String resourceId);

    /**
     * 上传白名单
     * @param whiteList 白名单
     * @param resourceId 资源ID
     * @return 上传结果
     */
    @PostMapping("/uploadWhiteList/{resourceId}")
    CommonResult<String> uploadWhiteList(@RequestBody List<String> whiteList, @PathVariable(name = "resourceId") String resourceId);

    /**
     * 获取认证中心服名称
     * @return 服务名称
     */
    @GetMapping("/api/base/getApplicationName")
    CommonResult<String> getApplicationName();
}
