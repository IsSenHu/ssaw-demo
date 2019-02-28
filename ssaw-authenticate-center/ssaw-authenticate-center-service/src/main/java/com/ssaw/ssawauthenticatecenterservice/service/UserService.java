package com.ssaw.ssawauthenticatecenterservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.vo.UpdateUserDto;
import com.ssaw.ssawauthenticatecenterfeign.vo.UserDto;
import com.ssaw.ssawauthenticatecenterfeign.vo.UserInfoDto;
import com.ssaw.ssawauthenticatecenterfeign.vo.UserLoginDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author HuSen.
 * @date 2018/12/11 9:58.
 */
public interface UserService extends UserDetailsService {
    /**
     * 分页查询用户
     * @param pageReq 分页查询请求参数
     * @return 分页结果
     */
    TableData<UserDto> page(PageReqDto<UserDto> pageReq);

    /**
     * 新增用户
     * @param userDto 新增用户请求对象
     * @return 新增结果
     */
    CommonResult<UserDto> add(UserDto userDto);

    /**
     * 通过用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    CommonResult<UpdateUserDto> findByUsername(String username);

    /**
     * 根据ID删除用户
     * @param id ID
     * @return 删除结果
     */
    CommonResult<Long> delete(Long id);

    /**
     * 修改用户
     * @param updateUserDto 修改用户请求对象
     * @return 修改结果
     */
    CommonResult<UserDto> update(UpdateUserDto updateUserDto);

    /**
     * 用户登录
     * @param userLoginDto 用户登录请求对象
     * @return 登录结果
     */
    CommonResult<UserInfoDto> login(UserLoginDto userLoginDto);

    /**
     * 注册系统内部后台用户接口
     * @param userDto 用户注册请求对象
     * @return 注册结果
     */
    CommonResult<String> register(UserDto userDto);


    /**
     * 根据ID查询用户
     * @param userId 用户ID
     * @return 用户
     */
    CommonResult<UserDto> findById(Long userId);

    /**
     * 用户登出
     * @param request HttpServletRequest
     * @return 登出结果
     */
    CommonResult<String> logout(HttpServletRequest request);
}
