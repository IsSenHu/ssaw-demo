package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.ssaw.commons.util.json.jack.JsonUtils;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.UpdateUserDto;
import com.ssaw.ssawauthenticatecenterservice.entity.RoleEntity;
import com.ssaw.ssawauthenticatecenterservice.entity.UserRoleEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.role.RoleRepository;
import com.ssaw.ssawauthenticatecenterservice.repository.user.UserRoleRepository;
import com.ssaw.ssawuserresourcefeign.dto.UserDto;
import com.ssaw.ssawuserresourcefeign.feign.UserFeign;
import com.ssaw.ssawauthenticatecenterservice.service.UserService;
import com.ssaw.ssawauthenticatecenterservice.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static com.ssaw.commons.constant.Constants.ResultCodes.ERROR;
import static com.ssaw.commons.constant.Constants.ResultCodes.SUCCESS;

/**
 * @author HuSen.
 * @date 2018/12/11 9:58.
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseService implements UserService {

    private final UserFeign userFeign;
    private final UserRoleRepository userRoleRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserFeign userFeign, UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
        this.userFeign = userFeign;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CommonResult<UserDto> commonResult = userFeign.findByUsername(username);
        log.info("查询到用户:{}",JsonUtils.object2JsonString(commonResult));
        if(NumberUtils.compare(SUCCESS, commonResult.getCode()) != 0) {
            throw new UsernameNotFoundException("用户名或密码错误!");
        }
        UserDto data = commonResult.getData();
        UserVo userVo = new UserVo();
        userVo.setId(data.getId());
        userVo.setUsername(data.getUsername());
        userVo.setPassword(data.getPassword());
        userVo.setGrantedAuthorities(new ArrayList<>(0));
        return userVo;
    }

    @Override
    public TableData<UserDto> page(PageReqDto<UserDto> pageReq) {
        return userFeign.page(pageReq);
    }

    @Override
    public CommonResult<UserDto> add(UserDto userDto) {
        return userFeign.save(userDto);
    }

    @Override
    public CommonResult<UpdateUserDto> findByUsername(String username) {
        CommonResult<UserDto> byUsername = userFeign.findByUsername(username);
        CommonResult<UpdateUserDto> result;
        UpdateUserDto updateUserDto = new UpdateUserDto();
        if(null != byUsername && byUsername.getCode() == SUCCESS) {
            UserRoleEntity byUserId = userRoleRepository.findByUserId(byUsername.getData().getId());
            if(null != byUserId) {
                Optional<RoleEntity> byId = roleRepository.findById(byUserId.getRoleId());
                byId.ifPresent(role -> {
                    updateUserDto.setRoleId(role.getId());
                    updateUserDto.setRoleName(role.getName());
                });
            }
            updateUserDto.setId(byUsername.getData().getId());
            updateUserDto.setUsername(byUsername.getData().getUsername());
            updateUserDto.setPassword(byUsername.getData().getPassword());
            updateUserDto.setIsEnable(byUsername.getData().getIsEnable());
            updateUserDto.setRealName(byUsername.getData().getRealName());
            updateUserDto.setDescription(byUsername.getData().getDescription());
            updateUserDto.setCreateTime(byUsername.getData().getCreateTime());
            updateUserDto.setUpdateTime(byUsername.getData().getUpdateTime());
            result = CommonResult.createResult(SUCCESS, "成功!", updateUserDto);
        } else {
            result = CommonResult.createResult(ERROR, "失败!", null);
        }
        return result;
    }

    @Override
    public CommonResult<Long> delete(Long id) {
        return userFeign.delete(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<UserDto> update(UpdateUserDto updateUserDto) {
        // 保存用户和角色的关系
        UserRoleEntity byUserId = userRoleRepository.findByUserId(updateUserDto.getId());
        UserRoleEntity userRoleEntity;
        if(null == byUserId) {
            userRoleEntity = new UserRoleEntity();
            userRoleEntity.setUserId(updateUserDto.getId());
            userRoleEntity.setRoleId(updateUserDto.getRoleId());
            userRoleEntity.setCreateTime(LocalDateTime.now());
        } else {
            userRoleEntity = byUserId;
        }
        userRoleRepository.save(userRoleEntity);

        // 调用用户服务修改用户
        UserDto userDto = new UserDto();
        userDto.setId(updateUserDto.getId());
        userDto.setUsername(updateUserDto.getUsername());
        userDto.setPassword(updateUserDto.getPassword());
        userDto.setIsEnable(updateUserDto.getIsEnable());
        userDto.setRealName(updateUserDto.getRealName());
        userDto.setDescription(updateUserDto.getDescription());
        CommonResult<UserDto> save = userFeign.save(userDto);
        if(save.getCode() != SUCCESS) {
            throw new RuntimeException("用户修改失败!");
        }
        return save;
    }
}
