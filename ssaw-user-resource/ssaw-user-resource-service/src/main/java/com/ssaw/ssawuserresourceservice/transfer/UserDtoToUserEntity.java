package com.ssaw.ssawuserresourceservice.transfer;

import com.ssaw.ssawuserresourcefeign.dto.UserDto;
import com.ssaw.ssawuserresourceservice.entity.UserEntity;
import org.springframework.stereotype.Component;
import java.util.function.Function;

/**
 * @author HuSen.
 * @date 2018/12/3 13:52.
 */
@Component
public class UserDtoToUserEntity implements Function<UserDto, UserEntity> {
    @Override
    public UserEntity apply(UserDto userDto) {
        UserEntity userEntity = null;
        if(null != userDto) {
            userEntity = new UserEntity();
            userEntity.setId(userDto.getId());
            userEntity.setUsername(userDto.getUsername());
            userEntity.setCreateTime(userDto.getCreateTime());
            userEntity.setDescription(userDto.getDescription());
            userEntity.setIsEnable(userDto.getIsEnable());
            userEntity.setPassword(userDto.getPassword());
            userEntity.setRealName(userDto.getRealName());
            userEntity.setUpdateTime(userDto.getUpdateTime());
        }
        return userEntity;
    }
}
