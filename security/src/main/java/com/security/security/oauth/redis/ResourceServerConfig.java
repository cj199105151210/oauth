package com.security.security.oauth.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
/**
 * 这里设置访问路径权限
 * 这里配置的拦截会和WebSecurityConfigurerAdapter配置的拦截起冲突，最好配置成一个，如果拆分成两个服务，应该可以解决这个问题
 */
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")          //默认加上前缀ROLE_,目前原因未知
                .antMatchers("/user/**").hasAnyRole("ADMIN","USER")
                .antMatchers("/test").authenticated()
                .anyRequest().authenticated();
    }

}
