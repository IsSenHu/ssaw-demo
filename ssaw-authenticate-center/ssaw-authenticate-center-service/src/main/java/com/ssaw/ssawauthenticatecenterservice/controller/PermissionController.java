package com.ssaw.ssawauthenticatecenterservice.controller;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
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
public class PermissionController extends BaseController {

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Validating
    @PostMapping("/add")
    @RequestLog(method = "PermissionController.add(PermissionDto permissionDto)")
    public CommonResult<PermissionDto> add(@RequestBody @Valid PermissionDto permissionDto, BindingResult result) {
        return permissionService.add(permissionDto);
    }

    @PostMapping("/page")
    @RequestLog(method = "PermissionController.page(PageReqDto<PermissionDto> pageReqDto)")
    public TableData<PermissionDto> page(@RequestBody PageReqDto<PermissionDto> pageReqDto) {
        return permissionService.page(pageReqDto);
    }

    @PostMapping("/delete/{id}")
    @RequestLog(method = "PermissionController.delete(Long id)")
    public CommonResult<Long> delete(@PathVariable(name = "id") Long id) {
        return permissionService.delete(id);
    }

    @Validating
    @PostMapping("/update")
    @RequestLog(method = "PermissionController.update(PermissionDto permissionDto)")
    public CommonResult<PermissionDto> update(@RequestBody @Valid PermissionDto permissionDto, BindingResult result) {
        return permissionService.update(permissionDto);
    }
}
