package com.ssaw.ssawauthenticatecenterservice.controller;

import com.ssaw.commons.annotations.RequestLog;
import com.ssaw.commons.annotations.Validating;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.UpdateUserDto;
import com.ssaw.ssawauthenticatecenterservice.service.UserService;
import com.ssaw.ssawuserresourcefeign.dto.UserDto;
import com.ssaw.ssawuserresourcefeign.feign.UserFeign;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.nio.charset.Charset;

/**
 * @author HuSen.
 * @date 2018/12/14 20:12.
 */
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {

    private final UserService userService;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestLog(method = "UserController.page(PageReqDto<UserDto> pageReq)")
    @PostMapping("/page")
    public TableData<UserDto> page(@RequestBody PageReqDto<UserDto> pageReq) {
        return userService.page(pageReq);
    }

    @Validating
    @PostMapping("/add")
    @RequestLog(method = "UserController.add(UserDto userDto)")
    public CommonResult<UserDto> add(@Valid @RequestBody UserDto userDto, BindingResult result) {
        return userService.add(userDto);
    }

    @PostMapping("/findByUsername/{username}")
    @RequestLog(method = "UserController.findByUsername(String username)")
    public CommonResult<UpdateUserDto> findByUsername(@PathVariable(value = "username") String username) {
        return userService.findByUsername(username);
    }

    @PostMapping("/delete/{id}")
    @RequestLog(method = "UserController.delete(Long id)")
    public CommonResult<Long> delete(@PathVariable(value = "id") Long id) {
        return userService.delete(id);
    }

    @Validating
    @PostMapping("/update")
    @RequestLog(method = "UserController.update(UpdateUserDto updateUserDto)")
    public CommonResult<UserDto> update(@Valid @RequestBody UpdateUserDto updateUserDto, BindingResult result) {
        return userService.update(updateUserDto);
    }

    @GetMapping("/testAuthenticate")
    public CommonResult<String> testAuthenticate() {
        return userFeign.authenticate();
    }
}
