package com.ssaw.ssawauthenticatecenterservice.controller;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawuserresourcefeign.dto.RoleDto;
import com.ssaw.ssawauthenticatecenterservice.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author HuSen.
 * @date 2018/11/27 19:00.
 */
@Slf4j
@RestController
@RequestMapping("/api/role")
public class RoleController extends BaseController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @RequestLog(method = "根据用户ID查询所有角色>>>[findAllRoleByUserId(Long userId)]")
    @GetMapping("/get/{userId}")
    public CommonResult<List<RoleDto>> findAllRoleByUserId(@PathVariable(name = "userId") Long userId) {
        return roleService.findAllRoleByUserId(userId);
    }

    @Validating
    @RequestLog(method = "新增或修改角色>>>[save(RoleDto roleDto, BindingResult result)]")
    @PostMapping("/save")
    public CommonResult<RoleDto> save(@RequestBody @Valid RoleDto roleDto, BindingResult result) {
        return roleService.save(roleDto);
    }

    @RequestLog(method = "删除角色>>>[delete(Long roleId)]")
    @PostMapping("/delete/{roleId}")
    public CommonResult<Long> delete(@PathVariable(name = "roleId") Long roleId) {
        return roleService.delete(roleId);
    }
}
