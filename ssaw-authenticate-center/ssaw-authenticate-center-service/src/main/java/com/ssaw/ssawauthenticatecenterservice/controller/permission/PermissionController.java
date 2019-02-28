package com.ssaw.ssawauthenticatecenterservice.controller.permission;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.annotations.Menu;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityApi;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityMethod;
import com.ssaw.ssawauthenticatecenterfeign.vo.PermissionDto;
import com.ssaw.ssawauthenticatecenterservice.controller.BaseController;
import com.ssaw.ssawauthenticatecenterservice.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 新增权限
     * @param permissionDto 新增权限请求对象
     * @return 新增结果
     */
    @Validating
    @PostMapping("/add")
    @RequestLog(desc = "新增权限")
    @SecurityMethod(antMatcher = "/api/permission/add", scope = "PERMISSION_CREATE", button = "PERMISSION_CREATE", buttonName = "添加")
    public CommonResult<PermissionDto> add(@RequestBody PermissionDto permissionDto) {
        return permissionService.add(permissionDto);
    }

    /**
     * 根据ID查询权限
     * @param id ID
     * @return 权限
     */
    @GetMapping("/findById/{id}")
    @RequestLog(desc = "根据ID查询权限")
    @SecurityMethod(antMatcher = "/api/permission/findById/*", scope = "PERMISSION_READ")
    public CommonResult<PermissionDto> findById(@PathVariable(name = "id") Long id) {
        return permissionService.findById(id);
    }

    /**
     * 分页查询权限
     * @param pageReqDto 分页查询参数
     * @return 分页结果
     */
    @PostMapping("/page")
    @RequestLog(desc = "分页查询权限")
    @SecurityMethod(antMatcher = "/api/permission/page", scope = "PERMISSION_READ", button = "PERMISSION_READ", buttonName = "搜索")
    public TableData<PermissionDto> page(@RequestBody PageReqDto<PermissionDto> pageReqDto) {
        return permissionService.page(pageReqDto);
    }

    /**
     * 根据ID删除权限
     * @param id ID
     * @return 删除结果
     */
    @PostMapping("/delete/{id}")
    @RequestLog(desc = "根据ID删除权限")
    @SecurityMethod(antMatcher = "/api/permission/delete/*", scope = "PERMISSION_DELETE", button = "PERMISSION_DELETE", buttonName = "删除")
    public CommonResult<Long> delete(@PathVariable(name = "id") Long id) {
        return permissionService.delete(id);
    }

    /**
     * 修改权限
     * @param permissionDto 修改请求对象
     * @return 修改结果
     */
    @Validating
    @PostMapping("/update")
    @RequestLog(desc = "修改权限")
    @SecurityMethod(antMatcher = "/api/permission/update", scope = "PERMISSION_UPDATE", button = "PERMISSION_UPDATE", buttonName = "编辑")
    public CommonResult<PermissionDto> update(@RequestBody PermissionDto permissionDto) {
        return permissionService.update(permissionDto);
    }
}
