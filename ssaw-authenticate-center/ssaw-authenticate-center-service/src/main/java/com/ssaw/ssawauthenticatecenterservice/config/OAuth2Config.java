package com.ssaw.ssawauthenticatecenterservice.config;

import com.ssaw.commons.util.json.jack.JsonUtils;
import com.ssaw.ssawauthenticatecenterservice.service.ClientService;
import com.ssaw.ssawauthenticatecenterservice.service.UserService;
import com.ssaw.ssawauthenticatecenterservice.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import java.util.Map;

/**
 * @author HuSen.
 * @date 2018/11/28 10:53.
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final RedisConnectionFactory redisConnectionFactory;
    private final UserService userService;
    private final ClientService clientService;

    @Autowired
    public OAuth2Config(AuthenticationManager authenticationManager, RedisConnectionFactory redisConnectionFactory, UserService userService, ClientService clientService) {
        this.authenticationManager = authenticationManager;
        this.redisConnectionFactory = redisConnectionFactory;
        this.userService = userService;
        this.clientService = clientService;
    }

    private static final String KEY_PAIR = "myauthenticatecenter";
    private static final String MY_PASS = "521428Slyt";
    private static final String KEY_STORE_PATH = "keystore.jks";

    /**
     * @return 使用非对称加密算法来对Token进行签名
     */
    @Bean
    public JwtAccessTokenConverter getJwtAccessTokenConverter() {
        final JwtAccessToken converter = new JwtAccessToken();
        // 导入证书
        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(new ClassPathResource(KEY_STORE_PATH), MY_PASS.toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair(KEY_PAIR));
        return converter;
    }

    /**
     * @return 使用Redis来存Token
     */
    @Bean
    public RedisTokenStore getRedisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * @param security 用来配置令牌端点(Token Endpoint)的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .realm("SSAW-AUTHENTICATE-CENTER")
                // 主要是让/oauth/token支持client_id以及client_secret作登录认证 要进行client校验就必须配置这个
                .allowFormAuthenticationForClients();
    }

    /**
     * @param endpoints 用来配置授权(authorization)以及令牌(token)的访问端点和令牌服务(token services)
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager)
                // 配置JwtAccessToken转换器
                .accessTokenConverter(getJwtAccessTokenConverter())
                // 配置TokenTore
                .tokenStore(getRedisTokenStore())
                // 配置UserDetailsServices
                .userDetailsService(userService)
                // 允许使用Get和Post方法访问端口
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }

    /**
     * 配置Client
     * @param clients ClientDetailsServiceConfigurer
     * @throws Exception Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientService);
    }

    /**
     * 自定义JwtToken转换器
     * @see org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
     * @author HuSen
     */
    public class JwtAccessToken extends JwtAccessTokenConverter {

        /**
         * 生成token
         * @param accessToken accessToken
         * @param authentication authentication
         * @return 生成的token
         */
        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
            DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
            // 设置额外的用户信息
            UserVo userVo = (UserVo) authentication.getPrincipal();
            userVo.setPassword(null);
            // 将用户信息添加到token额外信息中
            defaultOAuth2AccessToken.getAdditionalInformation().put("user_info", userVo);
            return defaultOAuth2AccessToken;
        }

        /**
         * 解析token
         * @param value tokenValue
         * @param map map
         * @return OAuth2AccessToken
         */
        @Override
        public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
            OAuth2AccessToken oAuth2AccessToken = super.extractAccessToken(value, map);
            convertData(oAuth2AccessToken, map);
            return oAuth2AccessToken;
        }

        /**
         * 数据转换
         * @param oAuth2AccessToken oAuth2AccessToken
         * @param map 额外信息
         */
        private void convertData(OAuth2AccessToken oAuth2AccessToken, Map<String, ?> map) {
            oAuth2AccessToken.getAdditionalInformation().put("user_info", convertUserData(map.get("user_info")));
        }

        /**
         * 获取用户数据
         * @param map 用户数据
         * @return UserVo
         */
        private UserVo convertUserData(Object map) {
            return JsonUtils.jsonString2Object(JsonUtils.object2JsonString(map), UserVo.class);
        }
    }
}
