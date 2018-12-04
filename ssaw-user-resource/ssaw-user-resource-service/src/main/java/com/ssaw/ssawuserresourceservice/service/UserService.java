package com.ssaw.ssawuserresourceservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawuserresourcefeign.dto.UserDto;

import java.util.List;

/**
 * @author HuSen.
 * @date 2018/11/27 19:00.
 */
public interface UserService {
    CommonResult<UserDto> findByUsername(String username);

    CommonResult<UserDto> save(UserDto userDto);

    CommonResult<Long> saveUserRoles(Long userId, List<Long> roleIds);

    CommonResult<Long> delete(Long userId);
}
