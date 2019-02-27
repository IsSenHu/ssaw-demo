package com.ssaw.ssawauthenticatecenterservice.controller;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.annotations.Menu;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityApi;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityMethod;
import com.ssaw.ssawauthenticatecenterfeign.dto.UpdateUserDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.UserDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.UserInfoDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.UserLoginDto;
import com.ssaw.ssawauthenticatecenterservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author HuSen.
 * @date 2018/12/14 20:12.
 */
@RestController
@RequestMapping("/api/user")
@SecurityApi(index = "2", group = "用户管理", menu = @Menu(index = "2-1", title = "用户", scope = "USER_MANAGE", to = "/authenticate/center/user"))
public class UserController extends BaseController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestLog(method = "UserController.page(PageReqDto<UserDto> pageReq)")
    @PostMapping("/page")
    @SecurityMethod(antMatcher = "/api/user/page", scope = "USER_READ", button = "USER_READ", buttonName = "搜索")
    public TableData<UserDto> page(@RequestBody PageReqDto<UserDto> pageReq) {
        return userService.page(pageReq);
    }

    @Validating
    @PostMapping("/add")
    @RequestLog(method = "UserController.add(UserDto userDto)")
    @SecurityMethod(antMatcher = "/api/user/add", scope = "USER_CREATE", button = "USER_CREATE", buttonName = "编辑")
    public CommonResult<UserDto> add(@Valid @RequestBody UserDto userDto, BindingResult result) {
        return userService.add(userDto);
    }

    @PostMapping("/findByUsername/{username}")
    @RequestLog(method = "UserController.findByUsername(String username)")
    @SecurityMethod(antMatcher = "/api/user/findByUsername/*", scope = "USER_READ")
    public CommonResult<UpdateUserDto> findByUsername(@PathVariable(value = "username") String username) {
        return userService.findByUsername(username);
    }

    @PostMapping("/delete/{id}")
    @RequestLog(method = "UserController.delete(Long id)")
    @SecurityMethod(antMatcher = "/api/user/delete/*", scope = "USER_DELETE", button = "USER_DELETE", buttonName = "删除")
    public CommonResult<Long> delete(@PathVariable(value = "id") Long id) {
        return userService.delete(id);
    }

    @Validating
    @PostMapping("/update")
    @RequestLog(method = "UserController.update(UpdateUserDto updateUserDto)")
    @SecurityMethod(antMatcher = "/api/user/update", scope = "USER_UPDATE", button = "USER_UPDATE", buttonName = "编辑")
    public CommonResult<UserDto> update(@Valid @RequestBody UpdateUserDto updateUserDto, BindingResult result) {
        return userService.update(updateUserDto);
    }

    @PostMapping("/login")
    @RequestLog(method = "UserController.login(UserLoginDto userLoginDto)")
    public CommonResult<UserInfoDto> login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    /**
     * 注册系统内部后台用户接口
     * @return 注册结果
     */
    @PostMapping("/register")
    @RequestLog(method = "UserController.register(UserDto userDto)")
    @SecurityMethod(antMatcher = "/api/user/register", scope = "USER_REGISTER")
    public CommonResult<String> register(@RequestBody UserDto userDto) {
        return userService.register(userDto);
    }

    @PostMapping("/logout")
    @RequestLog(method = "UserController.register(HttpServletRequest request)")
    public CommonResult<String> logout(HttpServletRequest request) {
        return userService.logout(request);
    }
}
