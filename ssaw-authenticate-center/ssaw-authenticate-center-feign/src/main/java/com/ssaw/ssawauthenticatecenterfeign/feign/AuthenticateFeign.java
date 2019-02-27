package com.ssaw.ssawauthenticatecenterfeign.feign;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.dto.Button;
import com.ssaw.ssawauthenticatecenterfeign.dto.Menu;
import com.ssaw.ssawauthenticatecenterfeign.dto.ResourceDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.ScopeDto;
import com.ssaw.ssawauthenticatecenterfeign.fallback.AuthenticateFeignFallback;
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
    CommonResult<String> authenticate(@RequestParam(name = "requestUri") String requestUri);

    /**
     * 新增一个资源
     * @param resourceDto 资源数据模型
     * @return 操作结果
     */
    @PostMapping("/api/resource/uploadResource")
    CommonResult<ResourceDto> uploadResource(@RequestBody ResourceDto resourceDto);

    /**
     * 批量上传Scope
     * @param scopeDtoList ScopeDtoList
     * @return 结果
     */
    @PostMapping("/api/scope/uploadScopes")
    CommonResult<String> uploadScopes(@RequestBody List<ScopeDto> scopeDtoList);

    /**
     * 上传菜单
     * @param menu 菜单
     * @param resourceId 资源ID
     * @return 上传结果
     */
    @PostMapping("/uploadMenus/{resourceId}")
    CommonResult<String> uploadMenus(@RequestBody Menu menu, @PathVariable(name = "resourceId") String resourceId);

    /**
     * 上传按钮
     * @param buttons 按钮
     * @param resourceId 资源ID
     * @return 上传结果
     */
    @PostMapping("/uploadButtons/{resourceId}")
    CommonResult<String> uploadButtons(@RequestBody List<Button> buttons, @PathVariable(name = "resourceId") String resourceId);

    /**
     * 上传白名单
     * @param whiteList 白名单
     * @param resourceId 资源ID
     * @return 上传结果
     */
    @PostMapping("/uploadWhiteList/{resourceId}")
    CommonResult<String> uploadWhiteList(@RequestBody List<String> whiteList, @PathVariable(name = "resourceId") String resourceId);
}
