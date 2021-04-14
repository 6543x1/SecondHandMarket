package com.jessie.SHMarket.configuration;

import com.jessie.SHMarket.service.UserService;
import com.jessie.SHMarket.service.impl.UserDetailServiceImpl;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private UserDetailServiceImpl theUserDetailService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth
                // 从数据库读取的用户进行身份认证
                .userDetailsService(theUserDetailService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .csrf()
                .csrfTokenRepository(new CookieCsrfTokenRepository())
                //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())//httpOnly怎么开？？
                .ignoringAntMatchers("/user/isLogin")
                .ignoringAntMatchers("/user/Register")
                .ignoringAntMatchers("/user/login")
                .ignoringAntMatchers("/user/editPwByMail")
        ;
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasAnyAuthority("admin")
                .antMatchers("/r/r2").hasAuthority("p2")
                .antMatchers("/r/**").authenticated()//基于url的权限管理
                .anyRequest().permitAll()
                .and()
                .formLogin()
                    .loginProcessingUrl("/user/login")
                    .successForwardUrl("/user/loginSuccess")
                    .failureForwardUrl("/user/loginError")
                .and()
                .logout()
                    .logoutSuccessUrl("/user/loginOut")
                .and()
                .exceptionHandling()
                    .accessDeniedPage("/user/noAccess")
                ;
    }
}
