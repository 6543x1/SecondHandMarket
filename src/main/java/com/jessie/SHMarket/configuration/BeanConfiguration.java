package com.jessie.SHMarket.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

import java.util.HashMap;

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

//如果直接以Bean方式注入converter，顺序无法确定
    //这也是为什么出现\的原因之一


}
