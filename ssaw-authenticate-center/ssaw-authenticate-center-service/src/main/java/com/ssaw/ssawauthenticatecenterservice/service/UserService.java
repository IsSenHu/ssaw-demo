package com.ssaw.ssawauthenticatecenterservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.UpdateUserDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.UserLoginDto;
import com.ssaw.ssawuserresourcefeign.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author HuSen.
 * @date 2018/12/11 9:58.
 */
public interface UserService extends UserDetailsService {
    TableData<UserDto> page(PageReqDto<UserDto> pageReq);

    CommonResult<UserDto> add(UserDto userDto);

    CommonResult<UpdateUserDto> findByUsername(String username);

    CommonResult<Long> delete(Long id);

    CommonResult<UserDto> update(UpdateUserDto updateUserDto);

    CommonResult<String> login(UserLoginDto userLoginDto);
}
