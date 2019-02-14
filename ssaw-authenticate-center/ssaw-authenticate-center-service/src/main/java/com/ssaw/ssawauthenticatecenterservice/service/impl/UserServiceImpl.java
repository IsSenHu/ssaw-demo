package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.ssaw.commons.util.app.ApplicationContextUtil;
import com.ssaw.commons.util.json.jack.JsonUtils;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.UpdateUserDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.UserLoginDto;
import com.ssaw.ssawauthenticatecenterservice.constants.ClientConstant;
import com.ssaw.ssawauthenticatecenterservice.entity.ClientDetailsEntity;
import com.ssaw.ssawauthenticatecenterservice.entity.RoleEntity;
import com.ssaw.ssawauthenticatecenterservice.entity.UserRoleEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.client.ClientRepository;
import com.ssaw.ssawauthenticatecenterservice.repository.role.RoleRepository;
import com.ssaw.ssawauthenticatecenterservice.repository.user.UserRoleRepository;
import com.ssaw.ssawuserresourcefeign.dto.UserDto;
import com.ssaw.ssawuserresourcefeign.feign.UserFeign;
import com.ssaw.ssawauthenticatecenterservice.service.UserService;
import com.ssaw.ssawauthenticatecenterservice.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.ssaw.commons.constant.Constants.ResultCodes.*;

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
    private final ClientRepository clientRepository;

    @Autowired
    public UserServiceImpl(UserFeign userFeign, UserRoleRepository userRoleRepository, RoleRepository roleRepository, ClientRepository clientRepository) {
        this.userFeign = userFeign;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.clientRepository = clientRepository;
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

    @Override
    public CommonResult<String> login(UserLoginDto userLoginDto) {
        UserDetails userDetails = loadUserByUsername(userLoginDto.getUsername());
        ConfigurableApplicationContext applicationContext = ApplicationContextUtil.applicationContext;
        if (!applicationContext.getBean(PasswordEncoder.class).matches(userLoginDto.getPassword(), userDetails.getPassword())) {
            return CommonResult.createResult(ERROR, "失败", null);
        }
        // 获取客户端信息
        Optional<ClientDetailsEntity> byId = clientRepository.findById(ClientConstant.CLIENT_PREFIX.concat(userLoginDto.getUsername()));
        if (!byId.isPresent()) {
            return CommonResult.createResult(ERROR, "失败", null);
        }
        ClientDetailsEntity clientDetailsEntity = byId.get();

        JwtAccessTokenConverter jwtAccessTokenConverter = applicationContext.getBean(JwtAccessTokenConverter.class);

        // DefaultOauth2AccessToken
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
        token.setExpiration(new Date(System.currentTimeMillis() + clientDetailsEntity.getAccessTokenValiditySeconds() * 1000));
        token.setTokenType(ClientConstant.BEARER);
        DefaultExpiringOAuth2RefreshToken refreshToken = new DefaultExpiringOAuth2RefreshToken(UUID.randomUUID().toString(), new Date(System.currentTimeMillis() + clientDetailsEntity.getRefreshTokenValiditySeconds() * 1000));
        token.setRefreshToken(refreshToken);
        token.setScope(clientDetailsEntity.getScope());

        // Oauth2Authentication
        String redirectUri = CollectionUtils.isEmpty(clientDetailsEntity.getRegisteredRedirectUri()) ? null : clientDetailsEntity.getRegisteredRedirectUri().iterator().next();

        Map<String, String> requestParameters = new HashMap<>(6);
        requestParameters.put("code", "stTiiB");
        requestParameters.put("grant_type", ClientConstant.AuthorizedGrantTypes.AUTHORIZATION_CODE.getValue());
        requestParameters.put("client_id", clientDetailsEntity.getClientId());
        requestParameters.put("client_secret", userLoginDto.getPassword());
        requestParameters.put("redirect_uri", redirectUri);
        requestParameters.put("response_type", "code");

        OAuth2Request oAuth2Request = new OAuth2Request(requestParameters, clientDetailsEntity.getClientId(), CollectionUtils.emptyCollection(), true,
                clientDetailsEntity.getScope(), clientDetailsEntity.getResourceIds(), redirectUri, ClientConstant.CODE, new HashMap<>(0));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, usernamePasswordAuthenticationToken);
        oAuth2Authentication.setAuthenticated(true);

        OAuth2AccessToken enhance = jwtAccessTokenConverter.enhance(token, oAuth2Authentication);

        // 保存到redis中去
        RedisTokenStore redisTokenStore = applicationContext.getBean(RedisTokenStore.class);
        redisTokenStore.storeAccessToken(enhance, oAuth2Authentication);

        return CommonResult.createResult(SUCCESS, "成功", enhance.getValue());
    }
}
