package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.ssaw.commons.util.json.jack.JsonUtils;
import com.ssaw.commons.vo.CommonResult;
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

import java.util.ArrayList;

import static com.ssaw.commons.constant.Constants.ResultCodes.SUCCESS;

/**
 * @author HuSen.
 * @date 2018/12/11 9:58.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserFeign userFeign;

    @Autowired
    public UserServiceImpl(UserFeign userFeign) {
        this.userFeign = userFeign;
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
}
