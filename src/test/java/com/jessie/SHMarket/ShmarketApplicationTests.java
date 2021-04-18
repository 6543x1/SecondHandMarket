package com.jessie.SHMarket;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@SpringBootTest
class ShmarketApplicationTests
{

    @Test
    void contextLoads()
    {
    }

    @Test
    void getEncodedPassword()
    {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("123456"));
        System.out.println(bCryptPasswordEncoder.encode("123"));
        //System.out.println(DigestUtils.md5DigestAsHex(("123"+"b040fe39-be75-472e-b751-925046be03e9").getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void testJWCHPost()
    {
        try
        {
            String postURL = "http://59.77.226.32/logincheck.asp";
            PostMethod postMethod = null;
            postMethod = new PostMethod(postURL);
            postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            postMethod.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            postMethod.setRequestHeader("Accept-Encoding", "gzip, deflate");
            postMethod.setRequestHeader("Referer", "http://jwch.fzu.edu.cn/");
            postMethod.setRequestHeader("Origin", "http://jwch.fzu.edu.cn");
            postMethod.setRequestHeader("Upgrade-Insecure-Requests", "1");
            postMethod.setRequestHeader("DNT", "1");
            postMethod.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
            NameValuePair[] data = {
                    new NameValuePair("muser", "031900000"),
                    new NameValuePair("passwd", "123456ops")

            };
            //参数设置，需要注意的就是里边不能传NULL，要传空字符串
            postMethod.setRequestBody(data);

            org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
            int response = httpClient.executeMethod(postMethod); // 执行POST方法
            String result = postMethod.getResponseBodyAsString();
            System.out.println(result);
            System.out.println(postMethod.getStatusCode());

        } catch (Exception e)
        {
            System.out.println("请求异常" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
