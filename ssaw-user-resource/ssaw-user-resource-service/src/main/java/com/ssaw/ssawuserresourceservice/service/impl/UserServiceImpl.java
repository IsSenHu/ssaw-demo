package com.ssaw.ssawuserresourceservice.service.impl;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawuserresourcefeign.dto.UserDto;
import com.ssaw.ssawuserresourceservice.entity.UserEntity;
import com.ssaw.ssawuserresourceservice.entity.UserRoleEntity;
import com.ssaw.ssawuserresourceservice.repository.UserRepository;
import com.ssaw.ssawuserresourceservice.repository.UserRoleRepository;
import com.ssaw.ssawuserresourceservice.service.BaseService;
import com.ssaw.ssawuserresourceservice.service.UserService;
import com.ssaw.ssawuserresourceservice.transfer.UserDtoToUserEntity;
import com.ssaw.ssawuserresourceservice.transfer.UserEntityToUserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ssaw.commons.constant.Constants.ResultCodes.*;

/**
 * @author HuSen.
 * @date 2018/11/27 19:39.
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseService implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserEntityToUserDto userEntityToUserDto;
    private final UserDtoToUserEntity userDtoToUserEntity;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserEntityToUserDto userEntityToUserDto, UserDtoToUserEntity userDtoToUserEntity, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userEntityToUserDto = userEntityToUserDto;
        this.userDtoToUserEntity = userDtoToUserEntity;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CommonResult<UserDto> findByUsername(String username) {
        if(StringUtils.isBlank(username)) {
            return createResult(PARAM_ERROR, "用户名不能为空!", null);
        }
        UserEntity userEntity = userRepository.findByUsername(username);
        UserDto userDto = userEntityToUserDto.apply(userEntity);
        if(Objects.isNull(userDto)) {
            return createResult(DATA_NOT_EXIST, "该用户不存在!", null);
        } else {
            return createResult(SUCCESS, "成功!", userDto);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<UserDto> save(UserDto userDto) {
        // 新增用户
        if(Objects.isNull(userDto.getId())) {
            UserEntity userEntity = userDtoToUserEntity.apply(userDto);
            // 密码加密
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            // 默认启用
            userEntity.setIsEnable(Boolean.TRUE);
            userEntity.setCreateTime(LocalDateTime.now());
            userRepository.save(userEntity);
            userDto.setCreateTime(userEntity.getCreateTime());
        }
        // 修改用户
        else {
            Optional<UserEntity> byId = userRepository.findById(userDto.getId());
            if(!byId.isPresent()) {
                return createResult(DATA_NOT_EXIST, "该用户不存在!", userDto);
            }
            UserEntity oldUser = byId.get();
            // 如果修改了用户名，则要判断用户名是否重复，密码也不在这里修改
            if(!StringUtils.equals(userDto.getUsername(), oldUser.getUsername())) {
                if(userRepository.countByUsername(userDto.getUsername()) > 0) {
                    return createResult(DATA_EXIST, "该用户名已存在!", userDto);
                }
                oldUser.setUsername(userDto.getUsername());
            }
            oldUser.setUpdateTime(LocalDateTime.now());
            oldUser.setRealName(userDto.getRealName());
            oldUser.setIsEnable(userDto.getIsEnable());
            oldUser.setDescription(userDto.getDescription());
            userRepository.save(oldUser);

            userDto.setCreateTime(oldUser.getCreateTime());
            userDto.setUpdateTime(oldUser.getUpdateTime());
        }
        return createResult(SUCCESS, "成功!", userDto);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<Long> saveUserRoles(Long userId, List<Long> roleIds) {
        if(Objects.isNull(userId)) {
            return createResult(PARAM_ERROR, "用户ID不能为空!", null);
        }
        userRoleRepository.deleteAllByUserId(userId);
        if(CollectionUtils.isNotEmpty(roleIds)) {
            List<UserRoleEntity> userRoleEntities = roleIds.stream().filter(roleId -> !Objects.isNull(roleId)).map(roleId -> {
                UserRoleEntity userRoleEntity = new UserRoleEntity();
                userRoleEntity.setUserId(userId);
                userRoleEntity.setRoleId(roleId);
                return userRoleEntity;
            }).collect(Collectors.toList());
            userRoleRepository.saveAll(userRoleEntities);
        }
        return createResult(SUCCESS, "成功!", userId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<Long> delete(Long userId) {
        if(Objects.isNull(userId)) {
            return createResult(PARAM_ERROR, "用户ID不能为空!", null);
        }
        // 删除用户，同时删除用户的角色
        userRepository.deleteById(userId);
        userRoleRepository.deleteAllByUserId(userId);
        return createResult(SUCCESS, "成功!", userId);
    }
}
