package com.jessie.SHMarket.configuration;

import com.jessie.SHMarket.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/*
 * springSecurity完善后应该就不需要了，感谢Interceptor一直以来提供的帮助
 * */
@Configuration
public class MyInterceptorConfig extends WebMvcConfigurationSupport
{
    @Autowired
    TokenInterceptor tokenInterceptor;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry)
//    {
//        registry.addInterceptor(tokenInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/auth")
//                .excludePathPatterns("/user/Register")
//                .excludePathPatterns("/error")
//        //注意：有时候仍然被拦截，可能是springSecurity的csrf生效导致error，而error被拦截了
//        ;
//        super.addInterceptors(registry);
//    }
}
