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

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
/**
 * 使用内存模式配置用户
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
        //方法二：
        auth.inMemoryAuthentication().withUser("user1").password(passwordEncoder().encode("123456")).roles("user");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.requestMatchers().antMatchers("/oauth/**","/login/**","/logout/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and()
                .formLogin().permitAll();
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

