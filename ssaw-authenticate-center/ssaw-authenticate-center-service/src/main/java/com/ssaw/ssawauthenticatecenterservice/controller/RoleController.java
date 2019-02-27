package com.ssaw.ssawauthenticatecenterservice.controller;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.annotations.Menu;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityApi;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityMethod;
import com.ssaw.ssawauthenticatecenterfeign.dto.EditRoleDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.RoleDto;
import com.ssaw.ssawauthenticatecenterservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * @author HuSen.
 * @date 2018/12/14 17:53.
 */
@RestController
@RequestMapping("/api/role")
@SecurityApi(index = "2", group = "用户管理", menu = @Menu(index = "2-2", title = "角色", scope = "ROLE_MANAGE", to = "/authenticate/center/role"))
public class RoleController extends BaseController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Validating
    @PostMapping("/add")
    @RequestLog(method = "RoleController.add(RoleDto roleDto)")
    @SecurityMethod(antMatcher = "/api/role/add", scope = "ROLE_CREATE", button = "ROLE_CREATE", buttonName = "添加")
    public CommonResult<RoleDto> add(@RequestBody @Valid RoleDto roleDto, BindingResult result) {
        return roleService.add(roleDto);
    }

    @GetMapping("/findById/{id}")
    @RequestLog(method = "RoleController.findById(Long id)")
    @SecurityMethod(antMatcher = "/api/role/findById/*", scope = "ROLE_READ")
    public CommonResult<EditRoleDto> findById(@PathVariable(name = "id") Long id) {
        return roleService.findById(id);
    }

    @PostMapping("/page")
    @RequestLog(method = "RoleController.page(PageReqDto<RoleDto> pageReqDto)")
    @SecurityMethod(antMatcher = "/api/role/page", scope = "ROLE_READ", button = "ROLE_READ", buttonName = "搜索")
    public TableData<RoleDto> page(@RequestBody PageReqDto<RoleDto> pageReqDto) {
        return roleService.page(pageReqDto);
    }

    @Validating
    @PostMapping("/update")
    @RequestLog(method = "RoleController.update(RoleDto roleDto)")
    @SecurityMethod(antMatcher = "/api/role/update", scope = "ROLE_UPDATE", button = "ROLE_UPDATE", buttonName = "编辑")
    public CommonResult<RoleDto> update(@RequestBody @Valid RoleDto roleDto, BindingResult result) {
        return roleService.update(roleDto);
    }

    @PostMapping("/delete/{id}")
    @RequestLog(method = "RoleController.delete(Long id)")
    @SecurityMethod(antMatcher = "/api/role/delete/*", scope = "ROLE_DELETE", button = "ROLE_DELETE", buttonName = "删除")
    public CommonResult<Long> delete(@PathVariable(value = "id") Long id) {
        return roleService.delete(id);
    }

    @GetMapping("/search/{role}")
    @RequestLog(method = "RoleController.search(String role)")
    @SecurityMethod(antMatcher = "/api/role/search/*", scope = "ROLE_READ")
    public CommonResult<List<RoleDto>> search(@PathVariable(name = "role") String role) {
        return roleService.search(role);
    }
}
