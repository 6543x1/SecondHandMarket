package com.jessie.SHMarket.configuration;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mail.SimpleMailMessage;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * 本类用于设置一些自定义的bean
 * */
@Configuration
public class BeanConfiguration
{
    //    好像这样等价于<bean id=...></bean>的形式，还行
    @Bean(name = "theNewMap")
    public HashMap<Integer, String> getNewMap()
    {
        HashMap<Integer,String> map=new HashMap<>();
        map.put(0,"伊拉克战损版本");
        map.put(1,"伊拉克成色");
        map.put(2,"九新");
        map.put(3,"九五新");
        map.put(4,"全新");
        return map;
    }
    @Bean(name="theImgSuffix")
    public HashMap<Integer,String> getImgSuffix(){
        HashMap<Integer,String> map=new HashMap<>();
        map.put(0,"jpg");
        map.put(1,"png");
        map.put(2,"heic");
        map.put(3,"gif");
        map.put(4,"jpeg");
        return map;
    }
    @Bean(name="findPwTemplate")
    public SimpleMailMessage simpleMailMessage(){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom("6543x1@outlook.com");
        simpleMailMessage.setSubject("找回密码（请勿回复）");
        return simpleMailMessage;
    }

    @Bean(name = "newOrderTemplate")
    public SimpleMailMessage NewOrderMailMessage()
    {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("6543x1@outlook.com");
        simpleMailMessage.setSubject("你的一个宝贝已经被拍下");
        return simpleMailMessage;
    }

    //jackson辣鸡，fastjson牛逼！
    @Bean
    public HttpMessageConverter configureMessageConverters()
    {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(
                // 保留map空的字段
                SerializerFeature.WriteMapNullValue,
                // 将String类型的null转成""
                SerializerFeature.WriteNullStringAsEmpty,
                // 将Number类型的null转成0
                SerializerFeature.WriteNullNumberAsZero,
                // 将List类型的null转成[]
                SerializerFeature.WriteNullListAsEmpty,
                // 将Boolean类型的null转成false
                SerializerFeature.WriteNullBooleanAsFalse,
                // 避免循环引用
                SerializerFeature.DisableCircularReferenceDetect);

        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        List<MediaType> mediaTypeList = new ArrayList<>();
        // 解决中文乱码问题，相当于在Controller上的@RequestMapping中加了个属性produces = "application/json"
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(mediaTypeList);
        return converter;
    }


}
