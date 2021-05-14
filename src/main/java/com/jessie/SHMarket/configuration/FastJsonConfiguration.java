package com.jessie.SHMarket.configuration;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FastJsonConfiguration implements WebMvcConfigurer
{
    //由于取舍，实际上这个类并没有产生效果，详情见最后面的注解。
    //为什么不注释掉代码呢？因为大片的注释太难看了（
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
    {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                // 防止循环引用
                SerializerFeature.DisableCircularReferenceDetect,
                // 空集合返回[],不返回null
                SerializerFeature.WriteNullListAsEmpty,
                // 空字符串返回"",不返回null
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteMapNullValue
        );
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        //处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        converters.add(fastJsonHttpMessageConverter);//加到0或者加到1就会出现BUG，看来不能这么做啊
        //但是加到最后一个，其实就相当于完全没有效果了，加了个寂寞
        //还有解决方法B：把String的application/json全部改成text/plain，这样也不会被重复解析
        //这样也可以保证Converter生效，同时String返回的JSON也正常
        //美中不足的是，这样POSTMAN就不会认成JSON了，然后没有换行挤在一起，不太好看
        //所以最后的决定是抛弃FASTJSON的converter，毕竟我需要在请求发生错误的时候返回错误信息
        //直接返回实体类，我看别人请求失败就直接NULL返回去 我觉得这样不太好
        //也不知道这样对不对，希望多多指教
        //那为什么不把这个删掉呢？因为我不想删掉，我想留下这个故事

    }
}
