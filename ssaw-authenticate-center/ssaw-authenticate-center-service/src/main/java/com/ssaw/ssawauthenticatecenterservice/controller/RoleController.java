package com.ssaw.ssawauthenticatecenterservice.controller;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
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
public class RoleController extends BaseController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Validating
    @PostMapping("/add")
    @RequestLog(method = "RoleController.add(RoleDto roleDto)")
    public CommonResult<RoleDto> add(@RequestBody @Valid RoleDto roleDto, BindingResult result) {
        return roleService.add(roleDto);
    }

    @GetMapping("/findById/{id}")
    @RequestLog(method = "RoleController.findById(Long id)")
    public CommonResult<EditRoleDto> findById(@PathVariable(name = "id") Long id) {
        return roleService.findById(id);
    }

    @PostMapping("/page")
    @RequestLog(method = "RoleController.page(PageReqDto<RoleDto> pageReqDto)")
    public TableData<RoleDto> page(@RequestBody PageReqDto<RoleDto> pageReqDto) {
        return roleService.page(pageReqDto);
    }

    @Validating
    @PostMapping("/update")
    @RequestLog(method = "RoleController.update(RoleDto roleDto)")
    public CommonResult<RoleDto> update(@RequestBody @Valid RoleDto roleDto, BindingResult result) {
        return roleService.update(roleDto);
    }

    @PostMapping("/delete/{id}")
    @RequestLog(method = "RoleController.delete(Long id)")
    public CommonResult<Long> delete(@PathVariable(value = "id") Long id) {
        return roleService.delete(id);
    }

    @GetMapping("/search/{role}")
    @RequestLog(method = "RoleController.search(String role)")
    public CommonResult<List<RoleDto>> search(@PathVariable(name = "role") String role) {
        return roleService.search(role);
    }
}
