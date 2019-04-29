package com.security.security.oauth.memory;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * spring security 的声明式安全授权有两种方式，一种是url 模式匹配的方式，一种是方法上使用注解声明权限，这里说第二种
 *
 * spring security默认是禁用注解的，要想开启注解，需要在继承WebSecurityConfigurerAdapter的类上加
 * @EnableGlobalMethodSecurity注解，并在该类中将AuthenticationManager定义为Bean
 */

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
/**
 * 1.spring-security对用户身份进行认证授权
 * 主要作用：是对用户身份通过用户名与密码的方式进行认证并且授权
 *
 *
 */
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected UserDetailsService userDetailsService(){
        //配置内存模式的用户
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user").password(new BCryptPasswordEncoder().encode("123456"))
                .roles("user").build());
        manager.createUser(User.withUsername("admin").password(new BCryptPasswordEncoder().encode("123456"))
                .roles("admin").build());
        return manager;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //配置用户在内存中
        //方法一：
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        //方法二：两个方法选用一个，注释掉方法一，无法获取到token,方法二注释，能正常获取，所以这个用户在存在密码中？
//        auth.inMemoryAuthentication()
//                .withUser("user1")
//                .password(passwordEncoder()
//                        .encode("123456"))
//                .roles("user");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login.html")  //允许访问登陆页面
                .loginProcessingUrl("/login") //允许登陆跳转
                .and().logout().logoutUrl("/logout").deleteCookies("JSESSIONID").permitAll()  //允许退出并删除session ID
                .and().authorizeRequests()    //任何请求都会被放行，都默认有权限
                .antMatchers("/login.html").permitAll()  //
                .anyRequest().authenticated()
                .and().csrf().disable();

//        http.csrf().disable();
//        http.requestMatchers().antMatchers("/oauth/**","/login/**","/logout/**")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/oauth/**").authenticated()
//                .and()
//                .formLogin().permitAll();
    }

    /**
     * spring5.x 需要注入一个PasswordEncoder，否则会报这个错There is no PasswordEncoder mapped for the id \"null\"
     * 加了密码加密，所有用到的密码都需要这个加密方法加密
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 需要配置这个支持password模式
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

/*
* 配置用户信息，保存在内存中。也可以自定义将用户数据保存在数据库中，实现UserDetailsService接口，进行认证与授权，略。
配置访问哪些URL需要授权。必须配置authorizeRequests()，否则启动报错，说是没有启用security技术。
注意，在这里的身份进行认证与授权没有涉及到OAuth的技术：
当访问要授权的URL时，请求会被DelegatingFilterProxy拦截，如果还没有授权，请求就会被重定向到登录界面。
在登录成功（身份认证并授权）后，请求被重定向至之前访问的URL。
*
*
*
* 下面是框架提供的URL路径：

/oauth/authorize    授权端点
/oauth/token    令牌端点
/oauth/confirm_access    用户批准授权的端点
/oauth/error    用于渲染授权服务器的错误
/oauth/check_token    资源服务器解码access token
/oauth/check_token    当使用JWT的时候，暴露公钥的端点
* */