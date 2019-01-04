package com.ssaw.ssawauthenticatecenterservice.controller;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawuserresourcefeign.dto.UserDto;
import com.ssaw.ssawauthenticatecenterservice.service.UserService;
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
@RequestMapping("/api/user")
public class UserController extends BaseController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestLog(method = "查询用户>>>[findByUsername(String username)]")
    @GetMapping("/get/{username}")
    public CommonResult<UserDto> findByUsername(@PathVariable(name = "username") String username) {
        return userService.findByUsername(username);
    }

    @Validating
    @RequestLog(method = "新增或修改用户>>>[save(UserDto userDto, BindingResult result)]")
    @PostMapping("/save")
    public CommonResult<UserDto> save(@RequestBody @Valid UserDto userDto, BindingResult result) {
        return userService.save(userDto);
    }

    @RequestLog(method = "删除用户>>>[delete(Long userId)]")
    @PostMapping("/delete/{userId}")
    public CommonResult<Long> delete(@PathVariable(name = "userId") Long userId) {
        return userService.delete(userId);
    }

    @RequestLog(method = "维护用户角色>>>[saveUserRoles(Long userId, List<Long> roleIds)]")
    @PostMapping("/saveUserRoles/{userId}")
    public CommonResult<Long> saveUserRoles(@PathVariable(name = "userId") Long userId, @RequestBody List<Long>  roleIds) {
        return userService.saveUserRoles(userId, roleIds);
    }

    @RequestLog(method = "分页查询用户>>>[page(PageReqDto<UserDto> pageReq)]")
    @PostMapping("/page")
    public TableData<UserDto> page(PageReqDto<UserDto> pageReq) {
        return userService.page(pageReq);
    }
}
