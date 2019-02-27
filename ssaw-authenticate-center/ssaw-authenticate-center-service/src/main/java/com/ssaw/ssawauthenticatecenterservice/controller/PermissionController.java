package com.ssaw.ssawauthenticatecenterservice.controller;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.annotations.Menu;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityApi;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityMethod;
import com.ssaw.ssawauthenticatecenterfeign.dto.PermissionDto;
import com.ssaw.ssawauthenticatecenterservice.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

/**
 * @author HuSen.
 * @date 2018/12/13 16:58.
 */
@RestController
@RequestMapping("/api/permission")
@SecurityApi(index = "2", group = "用户管理", menu = @Menu(index = "2-3", title = "权限", scope = "PERMISSION_MANAGE", to = "/authenticate/center/permission"))
public class PermissionController extends BaseController {

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Validating
    @PostMapping("/add")
    @RequestLog(method = "PermissionController.add(PermissionDto permissionDto)")
    @SecurityMethod(antMatcher = "/api/permission/add", scope = "PERMISSION_CREATE", button = "PERMISSION_CREATE", buttonName = "添加")
    public CommonResult<PermissionDto> add(@RequestBody @Valid PermissionDto permissionDto, BindingResult result) {
        return permissionService.add(permissionDto);
    }

    @GetMapping("/findById/{id}")
    @RequestLog(method = "PermissionController.findById(Long id)")
    @SecurityMethod(antMatcher = "/api/permission/findById/*", scope = "PERMISSION_READ")
    public CommonResult<PermissionDto> findById(@PathVariable(name = "id") Long id) {
        return permissionService.findById(id);
    }

    @PostMapping("/page")
    @RequestLog(method = "PermissionController.page(PageReqDto<PermissionDto> pageReqDto)")
    @SecurityMethod(antMatcher = "/api/permission/page", scope = "PERMISSION_READ", button = "PERMISSION_READ", buttonName = "搜索")
    public TableData<PermissionDto> page(@RequestBody PageReqDto<PermissionDto> pageReqDto) {
        return permissionService.page(pageReqDto);
    }

    @PostMapping("/delete/{id}")
    @RequestLog(method = "PermissionController.delete(Long id)")
    @SecurityMethod(antMatcher = "/api/permission/delete/*", scope = "PERMISSION_DELETE", button = "PERMISSION_DELETE", buttonName = "删除")
    public CommonResult<Long> delete(@PathVariable(name = "id") Long id) {
        return permissionService.delete(id);
    }

    @Validating
    @PostMapping("/update")
    @RequestLog(method = "PermissionController.update(PermissionDto permissionDto)")
    @SecurityMethod(antMatcher = "/api/permission/update", scope = "PERMISSION_UPDATE", button = "PERMISSION_UPDATE", buttonName = "编辑")
    public CommonResult<PermissionDto> update(@RequestBody @Valid PermissionDto permissionDto, BindingResult result) {
        return permissionService.update(permissionDto);
    }
}
