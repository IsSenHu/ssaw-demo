package com.ssaw.uaa.api;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.feign.AuthenticateFeign;
import com.ssaw.ssawauthenticatecenterfeign.vo.user.UserInfoVO;
import com.ssaw.ssawauthenticatecenterfeign.vo.user.UserLoginVO;
import com.ssaw.uaa.util.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @date 2019/4/28 10:21
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private AuthenticateFeign authenticateFeign;

    @PostMapping("/login")
    public CommonResult<UserInfoVO> login(@RequestBody UserLoginVO userLoginVO) {
        CommonResult<UserInfoVO> result = authenticateFeign.login(userLoginVO);
        result.getData().setToken(JwtUtil.generateToken(result.getData()));
        return result;
    }
}