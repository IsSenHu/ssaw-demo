package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.ssaw.commons.util.app.ApplicationContextUtil;
import com.ssaw.commons.util.bean.CopyUtil;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqVO;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.event.local.RefreshClientFinishEvent;
import com.ssaw.ssawauthenticatecenterfeign.util.UserUtils;
import com.ssaw.ssawauthenticatecenterfeign.vo.permission.PermissionVO;
import com.ssaw.ssawauthenticatecenterfeign.vo.user.*;
import com.ssaw.ssawauthenticatecenterservice.authentication.cache.CacheManager;
import com.ssaw.ssawauthenticatecenterservice.constants.client.ClientConstant;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.role.RoleEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.role.RolePermissionEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.user.UserEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.user.UserRoleEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.permission.PermissionRepository;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.role.RoleRepository;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.role.permission.RolePermissionRepository;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.user.UserRepository;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.user.UserRoleRepository;
import com.ssaw.ssawauthenticatecenterservice.properties.SpringSummerAutumnWinterManageProperties;
import com.ssaw.ssawauthenticatecenterservice.service.MenuService;
import com.ssaw.ssawauthenticatecenterservice.specification.UserSpecification;
import com.ssaw.ssawauthenticatecenterservice.transfer.PermissionTransfer;
import com.ssaw.ssawauthenticatecenterservice.service.UserService;
import com.ssaw.ssawauthenticatecenterservice.details.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final PermissionTransfer permissionTransfer;
    private final UserRepository userRepository;
    private final MenuService menuService;

    @Autowired
    public UserServiceImpl(UserRoleRepository userRoleRepository, RoleRepository roleRepository, RolePermissionRepository rolePermissionRepository, PermissionRepository permissionRepository, PermissionTransfer permissionTransfer, UserRepository userRepository, MenuService menuService) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionRepository = permissionRepository;
        this.permissionTransfer = permissionTransfer;
        this.userRepository = userRepository;
        this.menuService = menuService;
    }

    /**
     * 根据ID查询用户
     * @param userId 用户ID
     * @return 用户
     */
    @Override
    public CommonResult<UserVO> findById(Long userId) {
        if (null == userId) {
            return CommonResult.createResult(PARAM_ERROR, "用户ID必传!", null);
        }
        Optional<UserEntity> byId = userRepository.findById(userId);
        return byId
                .map(userEntity -> CommonResult.createResult(SUCCESS, "成功!", CopyUtil.copyProperties(userEntity, new UserVO())))
                .orElseGet(() -> CommonResult.createResult(ERROR, "该用户不存在!", null));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity byUsername = userRepository.findByUsername(username);
        if(Objects.isNull(byUsername)) {
            throw new UsernameNotFoundException("用户名或密码错误!");
        }
        UserDetailsImpl userDetailsImpl = new UserDetailsImpl();
        userDetailsImpl.setId(byUsername.getId());
        userDetailsImpl.setUsername(byUsername.getUsername());
        userDetailsImpl.setPassword(byUsername.getPassword());
        userDetailsImpl.setInner(byUsername.getInner());
        userDetailsImpl.setOtherInfo(StringUtils.isNotBlank(byUsername.getOtherInfo()) ? byUsername.getOtherInfo() : "{}");
        userDetailsImpl.setDescription(byUsername.getDescription());
        userDetailsImpl.setIsEnable(byUsername.getIsEnable());
        userDetailsImpl.setRealName(byUsername.getRealName());

        UserRoleEntity userRole = userRoleRepository.findByUserId(byUsername.getId());
        if (null != userRole) {
            List<RolePermissionEntity> rolePermissionEntityList = rolePermissionRepository.findAllByRoleId(userRole.getRoleId());
            if (CollectionUtils.isNotEmpty(rolePermissionEntityList)) {
                List<PermissionVO> permissionVOList = rolePermissionEntityList.stream().map(rolePermissionEntity -> permissionRepository.findById(rolePermissionEntity.getPermissionId()))
                        .filter(Optional::isPresent).map(Optional::get).map(permissionTransfer::entity2Dto).collect(Collectors.toList());
                Set<SimpleGrantedAuthority> simpleGrantedAuthorities = permissionVOList.stream().map(scope -> new SimpleGrantedAuthority(scope.getScopeName())).collect(Collectors.toSet());
                userDetailsImpl.setGrantedAuthorities(simpleGrantedAuthorities);
            }
        } else {
            userDetailsImpl.setGrantedAuthorities(new ArrayList<>(0));
        }
        return userDetailsImpl;
    }

    /**
     * 分页查询用户
     * @param pageReq 分页查询请求参数
     * @return 分页结果
     */
    @Override
    public TableData<UserVO> page(PageReqVO<QueryUserVO> pageReq) {
        Sort.Order order = Sort.Order.asc("username");
        Pageable pageable = getPageRequest(pageReq);
        Page<UserEntity> entityPage = userRepository.findAll(new UserSpecification(pageReq.getData()), pageable);
        List<UserVO> dtoList = entityPage.getContent().stream().map(input -> CopyUtil.copyProperties(input, new UserVO())).collect(Collectors.toList());
        TableData<UserVO> tableData = new TableData<>();
        tableData.setContent(dtoList);
        tableData.setPage(entityPage.getNumber() + 1);
        tableData.setSize(entityPage.getSize());
        tableData.setTotalPages(entityPage.getTotalPages());
        tableData.setTotals(entityPage.getTotalElements());
        return tableData;
    }

    /**
     * 新增用户
     * @param createUserVO 新增用户请求对象
     * @return 新增结果
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<CreateUserVO> add(CreateUserVO createUserVO) {
        UserEntity userEntity = CopyUtil.copyProperties(createUserVO, new UserEntity());
        // 密码加密
        userEntity.setPassword(ApplicationContextUtil.getBean(PasswordEncoder.class).encode(userEntity.getPassword()));
        // 默认启用
        userEntity.setIsEnable(Boolean.TRUE);
        userEntity.setCreateTime(LocalDateTime.now());
        userRepository.save(userEntity);
        createUserVO.setId(userEntity.getId());
        return createResult(SUCCESS, "成功", createUserVO);
    }

    /**
     * 通过用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    @Override
    public CommonResult<ShowUpdateUserVO> findByUsername(String username) {
        UserEntity byUsername = userRepository.findByUsername(username);
        CommonResult<ShowUpdateUserVO> result;
        ShowUpdateUserVO showUpdateUserVO = new ShowUpdateUserVO();
        if(null != byUsername) {
            UserRoleEntity byUserId = userRoleRepository.findByUserId(byUsername.getId());
            if(null != byUserId) {
                Optional<RoleEntity> byId = roleRepository.findById(byUserId.getRoleId());
                byId.ifPresent(role -> {
                    showUpdateUserVO.setRoleId(role.getId());
                    showUpdateUserVO.setRoleName(role.getName());
                });
            }
            showUpdateUserVO.setId(byUsername.getId());
            showUpdateUserVO.setUsername(byUsername.getUsername());
            showUpdateUserVO.setPassword(byUsername.getPassword());
            showUpdateUserVO.setIsEnable(byUsername.getIsEnable());
            showUpdateUserVO.setRealName(byUsername.getRealName());
            showUpdateUserVO.setDescription(byUsername.getDescription());
            showUpdateUserVO.setCreateTime(byUsername.getCreateTime());
            showUpdateUserVO.setUpdateTime(byUsername.getUpdateTime());
            showUpdateUserVO.setInner(byUsername.getInner());
            showUpdateUserVO.setOtherInfo(byUsername.getOtherInfo());

            result = createResult(SUCCESS, "成功!", showUpdateUserVO);
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
        Optional<UserEntity> byId = userRepository.findById(id);
        if (byId.isPresent() && byId.get().getInner()) {
            CacheManager.removeUser(byId.get().getUsername());
            userRepository.deleteById(id);
        }
        // TODO 删除用户，同时删除用户相关的其他信息
        return createResult(SUCCESS, "成功!", id);
    }

    /**
     * 修改用户
     * @param updateUserVO 修改用户请求对象
     * @return 修改结果
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<UpdateUserVO> update(UpdateUserVO updateUserVO) {
        // 保存用户和角色的关系
        UserRoleEntity byUserId = userRoleRepository.findByUserId(updateUserVO.getId());
        UserRoleEntity userRoleEntity;
        if(null == byUserId) {
            userRoleEntity = new UserRoleEntity();
            userRoleEntity.setUserId(updateUserVO.getId());
            userRoleEntity.setRoleId(updateUserVO.getRoleId());
            userRoleEntity.setCreateMan(UserUtils.getUser().getUsername());
            userRoleEntity.setCreateTime(LocalDateTime.now());
            userRoleRepository.save(userRoleEntity);
        } else {
            byUserId.setRoleId(updateUserVO.getRoleId());
            byUserId.setModifyTime(LocalDateTime.now());
            byUserId.setModifyMan(UserUtils.getUser().getUsername());
            userRoleRepository.save(byUserId);
        }
        // 调用用户服务修改用户
        Optional<UserEntity> byId = userRepository.findById(updateUserVO.getId());
        if(!byId.isPresent()) {
            return createResult(DATA_NOT_EXIST, "该用户不存在!", updateUserVO);
        }
        UserEntity oldUser = byId.get();
        if (!StringUtils.equals(oldUser.getUsername(), updateUserVO.getUsername())) {
            return createResult(ERROR, "用户名不能修改", updateUserVO);
        }
        oldUser.setUpdateTime(LocalDateTime.now());
        // TODO 是否内部用户 和 自定义信息
        oldUser.setRealName(updateUserVO.getRealName());
        oldUser.setIsEnable(updateUserVO.getIsEnable());
        oldUser.setDescription(updateUserVO.getDescription());
        userRepository.save(oldUser);
        if (oldUser.getInner()) {
            CacheManager.refreshUser(baseLogin(oldUser.getUsername()).getData());
        }
        return createResult(SUCCESS, "成功!", updateUserVO);
    }

    /**
     * 用户登录
     * @param userLoginVO 用户登录请求对象
     * @return 登录结果
     */
    @Override
    public CommonResult<UserInfoVO> login(UserLoginVO userLoginVO) {
        UserInfoVO user = CacheManager.getUser(userLoginVO.getUsername());
        if (Objects.isNull(user)) {
            return createResult(FORBIDDEN, "用户名或密码错误", null);
        }
        if (!ApplicationContextUtil.getBean(PasswordEncoder.class).matches(userLoginVO.getPassword(), user.getPassword())) {
            return createResult(FORBIDDEN, "用户名或密码错误", null);
        }
        // 隐藏密码
        user.setPassword(null);
        return createResult(SUCCESS, "登录成功", user);
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
            return createResult(ERROR, "失败", "没有token信息");
        } else {
            String token = StringUtils.substringBetween(authorization, "Bearer ");
            RedisTokenStore redisTokenStore = ApplicationContextUtil.getBean(RedisTokenStore.class);
            redisTokenStore.removeAccessToken(token);
            return createResult(SUCCESS, "成功", "登出成功");
        }
    }

    /**
     * 注册系统内部后台用户接口
     * @param createUserVO 用户注册请求对象
     * @return 注册结果
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<String> register(CreateUserVO createUserVO) {
        CommonResult<CreateUserVO> result = this.add(createUserVO);
        if (result.getCode() != SUCCESS) {
            throw new RuntimeException("注册用户失败");
        }
        CacheManager.refreshUser(baseLogin(createUserVO.getUsername()).getData());
        return createResult(SUCCESS, "成功", createUserVO.getUsername());
    }

    @EventListener(RefreshClientFinishEvent.class)
    public void loadUser() {
        log.info("开始加载系统内部用户......");
        List<UserEntity> allByInner = userRepository.findAllByInner(true);
        for (UserEntity entity : allByInner) {
            try {
                CacheManager.refreshUser(baseLogin(entity.getUsername()).getData());
            } catch (Exception e) {
                log.error("加载:{} - 用户失败:", entity.getUsername(), e);
            }
        }
        log.info("结束加载系统内部用户......");
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void loadUserTask() {
        log.info("开始加载系统内部用户......");
        List<UserEntity> allByInner = userRepository.findAllByInner(true);
        for (UserEntity entity : allByInner) {
            try {
                CacheManager.refreshUser(baseLogin(entity.getUsername()).getData());
            } catch (Exception e) {
                log.error("加载:{} - 用户失败:", entity.getUsername(), e);
            }
        }
        log.info("结束加载系统内部用户......");
    }

    private CommonResult<UserInfoVO> baseLogin(String username) {
        UserDetailsImpl userDetails = (UserDetailsImpl) loadUserByUsername(username);
        // 获取客户端配置
        SpringSummerAutumnWinterManageProperties manageProperties = ApplicationContextUtil.getBean(SpringSummerAutumnWinterManageProperties.class);
        JwtAccessTokenConverter jwtAccessTokenConverter = ApplicationContextUtil.getBean(JwtAccessTokenConverter.class);

        // DefaultOauth2AccessToken
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
        token.setExpiration(new Date(System.currentTimeMillis() + manageProperties.getClientExpire() * 1000));
        token.setTokenType(ClientConstant.BEARER);

        // 用户的权限
        Set<String> scope = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        Set<String> resourceIds = new HashSet<>();
        UserRoleEntity userRole = userRoleRepository.findByUserId(userDetails.getId());
        if (null != userRole) {
            List<RolePermissionEntity> rolePermissionEntityList = rolePermissionRepository.findAllByRoleId(userRole.getRoleId());
            if (CollectionUtils.isNotEmpty(rolePermissionEntityList)) {
                List<PermissionVO> permissionVOList = rolePermissionEntityList.stream().map(rolePermissionEntity -> permissionRepository.findById(rolePermissionEntity.getPermissionId()))
                        .filter(Optional::isPresent).map(Optional::get).map(permissionTransfer::entity2Dto).collect(Collectors.toList());
                resourceIds = permissionVOList.stream().map(PermissionVO::getResourceName).collect(Collectors.toSet());
            }
        }

        token.setScope(scope);

        // Oauth2Authentication
        String clientId = manageProperties.getClientId().concat(userDetails.getUsername());
        Map<String, String> requestParameters = new HashMap<>(6);
        requestParameters.put("code", UUID.randomUUID().toString());
        requestParameters.put("grant_type", ClientConstant.AuthorizedGrantTypes.AUTHORIZATION_CODE.getValue());
        requestParameters.put("client_id", clientId);
        requestParameters.put("client_secret", manageProperties.getClientSecret());
        requestParameters.put("redirect_uri", manageProperties.getClientRegisteredRedirectUris());
        requestParameters.put("response_type", "code");

        OAuth2Request oAuth2Request = new OAuth2Request(requestParameters, clientId, CollectionUtils.emptyCollection(), true,
                scope, resourceIds, manageProperties.getClientRegisteredRedirectUris(), ClientConstant.CODE, new HashMap<>(0));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, usernamePasswordAuthenticationToken);
        oAuth2Authentication.setAuthenticated(true);

        OAuth2AccessToken enhance = jwtAccessTokenConverter.enhance(token, oAuth2Authentication);

        // 移除旧Token
        RedisTokenStore redisTokenStore = ApplicationContextUtil.getBean(RedisTokenStore.class);
        Collection<OAuth2AccessToken> oldTokens = redisTokenStore.findTokensByClientId(clientId);
        oldTokens.forEach(redisTokenStore::removeAccessToken);

        // 保存新的Token
        redisTokenStore.storeAccessToken(enhance, oAuth2Authentication);
        return createResult(SUCCESS, "成功", new UserInfoVO(userDetails.getId(), userDetails.getUsername(), userDetails.getPassword(), scope, menuService.getMenus(scope, resourceIds), enhance.getValue(), JSON.parseObject(userDetails.getOtherInfo(), Map.class), menuService.getButtons(scope, resourceIds)));
    }
}
