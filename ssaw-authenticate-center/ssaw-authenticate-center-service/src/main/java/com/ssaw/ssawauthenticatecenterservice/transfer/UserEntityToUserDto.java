package com.ssaw.ssawauthenticatecenterservice.transfer;

import com.ssaw.ssawauthenticatecenterfeign.vo.UserDto;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.user.UserEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * @author HuSen.
 * @date 2018/12/3 13:36.
 */
@Component
public class UserEntityToUserDto implements Function<UserEntity, UserDto> {

    @Override
    public UserDto apply(UserEntity userEntity) {
        UserDto userDto = null;
        if(null != userEntity) {
            userDto = new UserDto();
            userDto.setId(userEntity.getId());
            userDto.setCreateTime(userEntity.getCreateTime());
            userDto.setDescription(userEntity.getDescription());
            userDto.setIsEnable(userEntity.getIsEnable());
            userDto.setPassword(userEntity.getPassword());
            userDto.setRealName(userEntity.getRealName());
            userDto.setUpdateTime(userEntity.getUpdateTime());
            userDto.setUsername(userEntity.getUsername());
        }
        return userDto;
    }
}
