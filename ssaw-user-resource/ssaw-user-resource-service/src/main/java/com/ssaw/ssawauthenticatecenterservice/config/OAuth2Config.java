//package com.ssaw.ssawauthenticatecenterservice.config;
//
//import com.ssaw.support.properties.StatisticalCertificationCenterClientProperties;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
//import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
//
///**
// * @author HuSen.
// * @date 2018/11/28 10:53.
// */
//@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
//@Configuration
//@EnableAuthorizationServer
//public class OAuth2Config extends AuthorizationServerConfigurerAdapter {
//
//    private final StatisticalCertificationCenterClientProperties properties;
//    private final AuthenticationManager authenticationManager;
//    private final RedisConnectionFactory redisConnectionFactory;
//    private final UserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;
//    @Autowired
//    public OAuth2Config(AuthenticationManager authenticationManager, RedisConnectionFactory redisConnectionFactory, UserDetailsService userDetailsService, StatisticalCertificationCenterClientProperties properties, PasswordEncoder passwordEncoder) {
//        this.authenticationManager = authenticationManager;
//        this.redisConnectionFactory = redisConnectionFactory;
//        this.userDetailsService = userDetailsService;
//        this.properties = properties;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    private static final String KEY_PAIR = "myuserresource";
//    private static final String MY_PASS = "mypass";
//    private static final String KEY_STORE_PATH = "keystore.jks";
//
//    /**
//     * @return 使用非对称加密算法来对Token进行签名
//     */
//    @Bean
//    public JwtAccessTokenConverter getJwtAccessTokenConverter() {
//        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        // 导入证书
//        KeyStoreKeyFactory keyStoreKeyFactory =
//                new KeyStoreKeyFactory(new ClassPathResource(KEY_STORE_PATH), MY_PASS.toCharArray());
//        converter.setKeyPair(keyStoreKeyFactory.getKeyPair(KEY_PAIR));
//        return converter;
//    }
//
//    /**
//     * @return 使用Redis来存Token
//     */
//    @Bean
//    public RedisTokenStore getRedisTokenStore() {
//        return new RedisTokenStore(redisConnectionFactory);
//    }
//
//    /**
//     * @param security 用来配置令牌端点(Token Endpoint)的安全约束
//     */
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) {
//        security
//                .realm(properties.getId())
//                // 主要是让/oauth/token支持client_id以及client_secret作登录认证 要进行client校验就必须配置这个
//                .allowFormAuthenticationForClients();
//    }
//
//    /**
//     * @param endpoints 用来配置授权(authorization)以及令牌(token)的访问端点和令牌服务(token services)
//     */
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
//        endpoints
//                .authenticationManager(authenticationManager)
//                // 配置JwtAccessToken转换器
//                .accessTokenConverter(getJwtAccessTokenConverter())
//                // 配置TokenTore
//                .tokenStore(getRedisTokenStore())
//                // 配置UserDetailsServices
//                .userDetailsService(userDetailsService)
//                // 允许使用Get和Post方法访问端口
//                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
//    }
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        String[] authorizedGrantTypes = properties.getAuthorizedGrantTypes().split(",");
//        String[] scopes = properties.getScopes().split(",");
//        clients.inMemory()
//                .withClient(properties.getId())
//                .secret(passwordEncoder.encode(properties.getSecret()))
//                .redirectUris(properties.getRedirectUrls())
//                .authorizedGrantTypes(authorizedGrantTypes)
//                .scopes(scopes)
//                .resourceIds(properties.getResourceIds())
//                .accessTokenValiditySeconds(properties.getAccessTokenValiditySeconds())
//                .refreshTokenValiditySeconds(properties.getRefreshTokenValiditySeconds());
//    }
//}
