package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.ssaw.commons.util.app.ApplicationContextUtil;
import com.ssaw.commons.util.json.jack.JsonUtils;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.vo.*;
import com.ssaw.ssawauthenticatecenterservice.constants.client.ClientConstant;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.client.ClientDetailsEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.role.RoleEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.role.RolePermissionEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.user.UserEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.user.UserRoleEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.client.ClientRepository;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.permission.PermissionRepository;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.role.RoleRepository;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.role.permission.RolePermissionRepository;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.user.UserRepository;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.user.UserRoleRepository;
import com.ssaw.ssawauthenticatecenterservice.service.MenuService;
import com.ssaw.ssawauthenticatecenterservice.specification.UserSpecification;
import com.ssaw.ssawauthenticatecenterservice.transfer.PermissionTransfer;
import com.ssaw.ssawauthenticatecenterservice.transfer.UserDtoToUserEntity;
import com.ssaw.ssawauthenticatecenterservice.transfer.UserEntityToUserDto;
import com.ssaw.ssawauthenticatecenterservice.service.UserService;
import com.ssaw.ssawauthenticatecenterservice.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.ssaw.commons.constant.Constants.ResultCodes.*;
import static com.ssaw.commons.vo.CommonResult.createResult;

/**
 * @author HuSen.
 * @date 2018/12/11 9:58.
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseService implements UserService {

    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final PermissionTransfer permissionTransfer;
    private final UserRepository userRepository;
    private final UserEntityToUserDto userEntityToUserDto;
    private final UserDtoToUserEntity userDtoToUserEntity;
    private final MenuService menuService;

    @Autowired
    public UserServiceImpl(UserRoleRepository userRoleRepository, RoleRepository roleRepository, ClientRepository clientRepository, RolePermissionRepository rolePermissionRepository, PermissionRepository permissionRepository, PermissionTransfer permissionTransfer, UserRepository userRepository, UserEntityToUserDto userEntityToUserDto, UserDtoToUserEntity userDtoToUserEntity, MenuService menuService) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.clientRepository = clientRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionRepository = permissionRepository;
        this.permissionTransfer = permissionTransfer;
        this.userRepository = userRepository;
        this.userEntityToUserDto = userEntityToUserDto;
        this.userDtoToUserEntity = userDtoToUserEntity;
        this.menuService = menuService;
    }

    /**
     * 根据ID查询用户
     * @param userId 用户ID
     * @return 用户
     */
    @Override
    public CommonResult<UserDto> findById(Long userId) {
        if (null == userId) {
            return CommonResult.createResult(PARAM_ERROR, "用户ID必传!", null);
        }
        Optional<UserEntity> byId = userRepository.findById(userId);
        return byId
                .map(userEntity -> CommonResult.createResult(SUCCESS, "成功!", userEntityToUserDto.apply(userEntity)))
                .orElseGet(() -> CommonResult.createResult(ERROR, "该用户不存在!", null));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity byUsername = userRepository.findByUsername(username);
        log.info("查询到用户:{}",JsonUtils.object2JsonString(byUsername));
        if(Objects.isNull(byUsername)) {
            throw new UsernameNotFoundException("用户名或密码错误!");
        }
        UserVo userVo = new UserVo();
        userVo.setId(byUsername.getId());
        userVo.setUsername(byUsername.getUsername());
        userVo.setPassword(byUsername.getPassword());

        UserRoleEntity userRole = userRoleRepository.findByUserId(byUsername.getId());
        if (null != userRole) {
            List<RolePermissionEntity> rolePermissionEntityList = rolePermissionRepository.findAllByRoleId(userRole.getRoleId());
            if (CollectionUtils.isNotEmpty(rolePermissionEntityList)) {
                List<PermissionDto> permissionDtoList = rolePermissionEntityList.stream().map(rolePermissionEntity -> permissionRepository.findById(rolePermissionEntity.getPermissionId()))
                        .filter(Optional::isPresent).map(Optional::get).map(permissionTransfer::entity2Dto).collect(Collectors.toList());
                Set<SimpleGrantedAuthority> simpleGrantedAuthorities = permissionDtoList.stream().map(scope -> new SimpleGrantedAuthority(scope.getScopeName())).collect(Collectors.toSet());
                userVo.setGrantedAuthorities(simpleGrantedAuthorities);
            }
        } else {
            userVo.setGrantedAuthorities(new ArrayList<>(0));
        }
        return userVo;
    }

    /**
     * 分页查询用户
     * @param pageReq 分页查询请求参数
     * @return 分页结果
     */
    @Override
    public TableData<UserDto> page(PageReqDto<UserDto> pageReq) {
        Sort.Order order = Sort.Order.asc("username");
        Pageable pageable = PageRequest.of(pageReq.getPage() - 1, pageReq.getSize(), Sort.by(order));
        Page<UserEntity> entityPage = userRepository.findAll(new UserSpecification(pageReq.getData()), pageable);
        List<UserDto> dtoList = entityPage.getContent().stream().map(userEntityToUserDto).collect(Collectors.toList());
        log.info("分页查询参数:{},查询结果:{}", JSON.toJSONString(pageable), JSON.toJSONString(dtoList));
        TableData<UserDto> tableData = new TableData<>();
        tableData.setContent(dtoList);
        tableData.setPage(entityPage.getNumber() + 1);
        tableData.setSize(entityPage.getSize());
        tableData.setTotalPages(entityPage.getTotalPages());
        tableData.setTotals(entityPage.getTotalElements());
        return tableData;
    }

    /**
     * 新增用户
     * @param userDto 新增用户请求对象
     * @return 新增结果
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<UserDto> add(UserDto userDto) {
        UserEntity userEntity = userDtoToUserEntity.apply(userDto);
        // 密码加密
        userEntity.setPassword(ApplicationContextUtil.getBean(PasswordEncoder.class).encode(userEntity.getPassword()));
        // 默认启用
        userEntity.setIsEnable(Boolean.TRUE);
        userEntity.setCreateTime(LocalDateTime.now());
        UserEntity save = userRepository.save(userEntity);
        userDto.setCreateTime(userEntity.getCreateTime());
        userDto.setId(save.getId());
        return createResult(SUCCESS, "成功", userDto);
    }

    /**
     * 通过用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    @Override
    public CommonResult<UpdateUserDto> findByUsername(String username) {
        UserEntity byUsername = userRepository.findByUsername(username);
        CommonResult<UpdateUserDto> result;
        UpdateUserDto updateUserDto = new UpdateUserDto();
        if(null != byUsername) {
            UserRoleEntity byUserId = userRoleRepository.findByUserId(byUsername.getId());
            if(null != byUserId) {
                Optional<RoleEntity> byId = roleRepository.findById(byUserId.getRoleId());
                byId.ifPresent(role -> {
                    updateUserDto.setRoleId(role.getId());
                    updateUserDto.setRoleName(role.getName());
                });
            }
            updateUserDto.setId(byUsername.getId());
            updateUserDto.setUsername(byUsername.getUsername());
            updateUserDto.setPassword(byUsername.getPassword());
            updateUserDto.setIsEnable(byUsername.getIsEnable());
            updateUserDto.setRealName(byUsername.getRealName());
            updateUserDto.setDescription(byUsername.getDescription());
            updateUserDto.setCreateTime(byUsername.getCreateTime());
            updateUserDto.setUpdateTime(byUsername.getUpdateTime());
            result = createResult(SUCCESS, "成功!", updateUserDto);
        } else {
            result = createResult(ERROR, "失败!", null);
        }
        return result;
    }

    /**
     * 根据ID删除用户
     * @param id ID
     * @return 删除结果
     */
    @Override
    public CommonResult<Long> delete(Long id) {
        if(Objects.isNull(id)) {
            return createResult(PARAM_ERROR, "用户ID不能为空!", null);
        }
        userRepository.deleteById(id);
        // TODO 删除用户，同时删除用户相关的其他信息
        return createResult(SUCCESS, "成功!", id);
    }

    /**
     * 修改用户
     * @param updateUserDto 修改用户请求对象
     * @return 修改结果
     */
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
        // TODO 用户名不能修改
        // 调用用户服务修改用户
        UserDto userDto = new UserDto();
        userDto.setId(updateUserDto.getId());
        userDto.setUsername(updateUserDto.getUsername());
        userDto.setPassword(updateUserDto.getPassword());
        userDto.setIsEnable(updateUserDto.getIsEnable());
        userDto.setRealName(updateUserDto.getRealName());
        userDto.setDescription(updateUserDto.getDescription());
        CommonResult<UserDto> save = this.update(userDto);
        if(save.getCode() != SUCCESS) {
            throw new RuntimeException("用户修改失败!");
        }

        // 重新设置Client的资源ID和作用域
        Optional<ClientDetailsEntity> byId = clientRepository.findById(ClientConstant.CLIENT_PREFIX.concat(updateUserDto.getUsername()));
        if (byId.isPresent()) {
            List<RolePermissionEntity> rolePermissionEntityList = rolePermissionRepository.findAllByRoleId(updateUserDto.getRoleId());
            List<PermissionDto> permissionDtoList = rolePermissionEntityList.stream().map(rolePermissionEntity -> permissionRepository.findById(rolePermissionEntity.getPermissionId()))
                    .filter(Optional::isPresent).map(Optional::get).map(permissionTransfer::entity2Dto).collect(Collectors.toList());
            Set<String> scopes = permissionDtoList.stream().map(PermissionDto::getScopeName).collect(Collectors.toSet());
            Set<String> resourceIds = permissionDtoList.stream().map(PermissionDto::getResourceName).collect(Collectors.toSet());
            ClientDetailsEntity clientDetailsEntity = byId.get();
            clientDetailsEntity.setScopes(String.join("," ,scopes.toArray(new String[]{})));
            clientDetailsEntity.setResourceIds(String.join("," ,resourceIds.toArray(new String[]{})));
        }
        return save;
    }

    private CommonResult<UserDto> update(UserDto userDto) {
        Optional<UserEntity> byId = userRepository.findById(userDto.getId());
        if(!byId.isPresent()) {
            return createResult(DATA_NOT_EXIST, "该用户不存在!", userDto);
        }
        UserEntity oldUser = byId.get();
        if (!StringUtils.equals(oldUser.getUsername(), userDto.getUsername())) {
            return CommonResult.createResult(ERROR, "用户名不能修改", userDto);
        }
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
        return createResult(SUCCESS, "成功!", userDto);
    }

    /**
     * 用户登录
     * @param userLoginDto 用户登录请求对象
     * @return 登录结果
     */
    @Override
    public CommonResult<UserInfoDto> login(UserLoginDto userLoginDto) {
        UserVo userDetails = (UserVo) loadUserByUsername(userLoginDto.getUsername());
        if (!ApplicationContextUtil.getBean(PasswordEncoder.class).matches(userLoginDto.getPassword(), userDetails.getPassword())) {
            return createResult(ERROR, "失败", null);
        }
        // 获取客户端信息
        Optional<ClientDetailsEntity> byId = clientRepository.findById(ClientConstant.CLIENT_PREFIX.concat(userLoginDto.getUsername()));
        if (!byId.isPresent()) {
            return createResult(ERROR, "失败", null);
        }
        ClientDetailsEntity clientDetailsEntity = byId.get();

        JwtAccessTokenConverter jwtAccessTokenConverter = ApplicationContextUtil.getBean(JwtAccessTokenConverter.class);

        // DefaultOauth2AccessToken
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
        token.setExpiration(new Date(System.currentTimeMillis() + clientDetailsEntity.getAccessTokenValiditySeconds() * 1000));
        token.setTokenType(ClientConstant.BEARER);
        DefaultExpiringOAuth2RefreshToken refreshToken = new DefaultExpiringOAuth2RefreshToken(UUID.randomUUID().toString(), new Date(System.currentTimeMillis() + clientDetailsEntity.getRefreshTokenValiditySeconds() * 1000));
        token.setRefreshToken(refreshToken);

        // 用户的权限
        Set<String> scope = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        Set<String> resourceIds = new HashSet<>();
        UserRoleEntity userRole = userRoleRepository.findByUserId(userDetails.getId());
        if (null != userRole) {
            List<RolePermissionEntity> rolePermissionEntityList = rolePermissionRepository.findAllByRoleId(userRole.getRoleId());
            if (CollectionUtils.isNotEmpty(rolePermissionEntityList)) {
                List<PermissionDto> permissionDtoList = rolePermissionEntityList.stream().map(rolePermissionEntity -> permissionRepository.findById(rolePermissionEntity.getPermissionId()))
                        .filter(Optional::isPresent).map(Optional::get).map(permissionTransfer::entity2Dto).collect(Collectors.toList());
                resourceIds = permissionDtoList.stream().map(PermissionDto::getResourceName).collect(Collectors.toSet());
            }
        }

        token.setScope(scope);

        // Oauth2Authentication
        String redirectUri = CollectionUtils.isEmpty(clientDetailsEntity.getRegisteredRedirectUri()) ? null : clientDetailsEntity.getRegisteredRedirectUri().iterator().next();

        Map<String, String> requestParameters = new HashMap<>(6);
        requestParameters.put("code", UUID.randomUUID().toString());
        requestParameters.put("grant_type", ClientConstant.AuthorizedGrantTypes.AUTHORIZATION_CODE.getValue());
        requestParameters.put("client_id", clientDetailsEntity.getClientId());
        requestParameters.put("client_secret", userLoginDto.getPassword());
        requestParameters.put("redirect_uri", redirectUri);
        requestParameters.put("response_type", "code");

        OAuth2Request oAuth2Request = new OAuth2Request(requestParameters, clientDetailsEntity.getClientId(), CollectionUtils.emptyCollection(), true,
                scope, resourceIds, redirectUri, ClientConstant.CODE, new HashMap<>(0));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, usernamePasswordAuthenticationToken);
        oAuth2Authentication.setAuthenticated(true);

        OAuth2AccessToken enhance = jwtAccessTokenConverter.enhance(token, oAuth2Authentication);

        // 移除旧Token
        RedisTokenStore redisTokenStore = ApplicationContextUtil.getBean(RedisTokenStore.class);
        Collection<OAuth2AccessToken> oldTokens = redisTokenStore.findTokensByClientId(clientDetailsEntity.getClientId());
        oldTokens.forEach(redisTokenStore::removeAccessToken);

        // 保存新的Token
        redisTokenStore.storeAccessToken(enhance, oAuth2Authentication);
        return createResult(SUCCESS, "成功", new UserInfoDto(userDetails.getId(), userDetails.getUsername(), scope, menuService.getMenus(scope, resourceIds), enhance.getValue(), null, menuService.getButtons(scope, resourceIds)));
    }

    /**
     * 用户登出
     * @param request HttpServletRequest
     * @return 登出结果
     */
    @Override
    public CommonResult<String> logout(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(authorization)) {
            return CommonResult.createResult(ERROR, "失败", "没有token信息");
        } else {
            String token = StringUtils.substringBetween(authorization, "Bearer ");
            RedisTokenStore redisTokenStore = ApplicationContextUtil.getBean(RedisTokenStore.class);
            redisTokenStore.removeAccessToken(token);
            return CommonResult.createResult(SUCCESS, "成功", "登出成功");
        }
    }

    /**
     * 注册系统内部后台用户接口
     * @param userDto 用户注册请求对象
     * @return 注册结果
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<String> register(UserDto userDto) {
        CommonResult<UserDto> result = this.add(userDto);
        if (result.getCode() != SUCCESS) {
            throw new RuntimeException("注册用户失败");
        }
        ClientDetailsEntity clientDetailsEntity = new ClientDetailsEntity();
        clientDetailsEntity.setUserId(result.getData().getId());
        clientDetailsEntity.setClientId(ClientConstant.CLIENT_PREFIX.concat(userDto.getUsername()));
        clientDetailsEntity.setClientSecret(ApplicationContextUtil.getBean(PasswordEncoder.class).encode(userDto.getPassword()));
        clientDetailsEntity.setCreateTime(LocalDateTime.now());
        clientDetailsEntity.setAuthorizedGrantTypes(ClientConstant.AuthorizedGrantTypes.AUTHORIZATION_CODE.getValue());
        clientDetailsEntity.setAccessTokenValiditySeconds(ClientConstant.LOGIN_TIME);
        clientDetailsEntity.setRefreshTokenValiditySeconds(ClientConstant.LOGIN_TIME);
        clientRepository.save(clientDetailsEntity);
        return createResult(SUCCESS, "成功", userDto.getUsername());
    }
}
