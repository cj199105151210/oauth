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

@Configuration
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
       security.realm("oauth2-resource")
               .tokenKeyAccess("permitAll()")
               .checkTokenAccess("isAuthenticated()")
               .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("demoClient")
                .secret(new BCryptPasswordEncoder().encode("demoSecret"))
                .redirectUris("https://www.baidu.com")
                .authorizedGrantTypes("authorization_code","client_credentials", "password", "refresh_token")
                .scopes("all")
                .resourceIds("oauth2-resource")
                .accessTokenValiditySeconds(60*60)
                .refreshTokenValiditySeconds(60*60);
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST);
    }
}
