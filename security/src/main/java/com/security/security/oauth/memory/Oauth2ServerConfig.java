package com.security.security.oauth.memory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * OAuth2的授权认证服务
 */
@Configuration
@EnableAuthorizationServer  //被用来配置授权服务器
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 用来配置令牌端点的安全约束
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
       security.realm("oauth2-resource")
               .tokenKeyAccess("permitAll()")
               .checkTokenAccess("isAuthenticated()")
               .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //添加客户端信息
        //使用内存存储OAUTH客户端信息
        clients.inMemory()
                .withClient("demoClient")
                .secret(new BCryptPasswordEncoder().encode("demoSecret"))
                //回调uri,在authorization_code 与implicit授权方式时，用以接受服务器返回的信息
                .redirectUris("https://www.baidu.com")
                //该client允许授权类型，不同的类型，则获得的token的方式不一样
                .authorizedGrantTypes("authorization_code","client_credentials", "password", "refresh_token")
                //允许授权的范围
                .scopes("all")
                .resourceIds("oauth2-resource")
                .accessTokenValiditySeconds(12*60*60)
                .refreshTokenValiditySeconds(12*60*60);
    }

    /**
     * 认证管理器：当你选择了资源所有者的密码(password)授权类型的时候，请设置这个属性并注入一个AuthenticationManager对象
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * https://www.cnblogs.com/cjsblog/p/9184173.html
     *
     *1.为了实现OAuth2授权服务器，在spring security的过滤器链中需要有以下端点
     * AuthorizationEndpoint 用于服务授权请求，默认URL是  /oauth/authorize
     * TokenEndpoint 用于服务访问令牌请求，默认url是  /oauth/token
     *2.在OAuth2 的资源服务器中需要实现下列过滤器
     * OAuth2AuthenticationProcessingFilter y用于加载认证
     * 对于所有的OAuth2.0 Provider特性，最简单的配置是用spring Oauth @Configuration 适配器
     *
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST);

    }
}
