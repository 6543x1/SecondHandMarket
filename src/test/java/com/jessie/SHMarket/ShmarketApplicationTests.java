package com.jessie.SHMarket;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SpringBootTest
class ShmarketApplicationTests
{

//    @Test
//    void contextLoads()
//    {
//    }
//
//    @Test
//    void getEncodedPassword()
//    {
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        System.out.println(bCryptPasswordEncoder.encode("123456"));
//        System.out.println(bCryptPasswordEncoder.encode("123"));
//        //System.out.println(DigestUtils.md5DigestAsHex(("123"+"b040fe39-be75-472e-b751-925046be03e9").getBytes(StandardCharsets.UTF_8)));
//    }
//
//    @Test
//    void testJWCHPost()
//    {
//        try
//        {
//            String postURL = "http://59.77.226.32/logincheck.asp";
//            PostMethod postMethod = null;
//            postMethod = new PostMethod(postURL);
//            postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
//            postMethod.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//            postMethod.setRequestHeader("Accept-Encoding", "gzip, deflate");
//            postMethod.setRequestHeader("Referer", "http://jwch.fzu.edu.cn/");
//            postMethod.setRequestHeader("Origin", "http://jwch.fzu.edu.cn");
//            postMethod.setRequestHeader("Upgrade-Insecure-Requests", "1");
//            postMethod.setRequestHeader("DNT", "1");
//            postMethod.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
//            NameValuePair[] data = {
//                    new NameValuePair("muser", "031902410"),
//                    new NameValuePair("passwd", "123456cnm")
//
//            };
//            //参数设置，需要注意的就是里边不能传NULL，要传空字符串
//            postMethod.setRequestBody(data);
//
//            org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
//            int response = httpClient.executeMethod(postMethod); // 执行POST方法
//            String result = postMethod.getResponseBodyAsString();
//            System.out.println(result);
//            System.out.println(postMethod.getStatusCode());
//
//        } catch (Exception e)
//        {
//            System.out.println("请求异常" + e.getMessage());
//            throw new RuntimeException(e.getMessage());
//        }
//    }
    @Test
    void jwcYzmToImg(){
        BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        try
        {
            byte[] bytes=decoder.decodeBuffer("BM¦\b6(H \u0001\u0018p\b\u0012\u000B\u0012\u000Búúÿúúÿúúÿ––––úúÿúúÿúúÿúúÿúúÿúúÿ–ú–ú–ú–úúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ––––úúÿúúÿúúÿúúÿúúÿúúÿ––––––––úúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ––úúÿúúÿúúÿúúÿúúÿúúÿúúÿ––úúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿ–úúúÿúúÿúúÿúúÿ–ú–úúúÿúúÿúúÿúúÿúúÿ–(xúúÿúúÿúúÿúúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿ––úúÿúúÿúúÿúúÿ––úúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ––úúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿ–úúÿ–úúÿúúÿ–úúúÿúúÿúúÿúúÿ–úúúÿúúÿúúÿúúÿúúÿúúÿ–(xúúÿúúÿúúÿúúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿ––úúÿúúÿúúÿúúÿ––úúÿúúÿúúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ–úúÿ–úúÿúúÿúúÿúúÿ–úúúÿúúÿúúÿ–ú–úúúÿúúÿúúÿúúÿúúÿúúÿ–(xúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿ––úúÿ––––úúÿ––úúÿúúÿúúÿ––––––––úúÿúúÿúúÿúúÿúúÿ––úúÿúúÿúúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿúúÿ–ú–úúúÿúúÿúúÿ–úúúÿúúÿúúÿúúÿúúÿúúÿ–(xúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ–––úúÿúúÿ––úúÿ––––úúÿ––úúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ––úúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ––úúÿúúÿúúÿúúÿúúÿúúÿ–úúúÿ–ú–ú–ú–úúúÿúúÿúúÿ–(x–(x–(x–(x–(x–(x–(xúúÿúúÿúúÿúúÿ––úúÿúúÿúúÿ–úúÿúúÿúúÿúúÿ––––––––úúÿ––úúÿúúÿúúÿúúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ––úúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿ–ú–úúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ–(xúúÿúúÿúúÿúúÿúúÿúúÿúúÿ–úúÿ–––úúÿúúÿúúÿúúÿúúÿ––úúÿ––––úúÿ––úúÿúúÿúúÿ––––––––úúÿúúÿ–úúÿúúÿúúÿúúÿ––úúÿúúÿúúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿ–úúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ–(xúúÿúúÿúúÿúúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ––úúÿúúÿúúÿ––––úúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿúúÿ–úúúÿúúÿúúÿ–úúúÿúúÿúúÿúúÿúúÿúúÿ–(xúúÿúúÿúúÿúúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ––úúÿúúÿúúÿúúÿ––úúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ––úúÿúúÿ––úúÿúúÿúúÿúúÿúúÿúúÿúúÿ––––úúÿúúÿúúÿúúÿúúÿúúÿúúÿ–ú–ú–úúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ––––––úúÿúúÿúúÿúúÿúúÿ––––––––úúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿúúÿ–úúÿúúÿúúÿúúÿúúÿ––––úúÿúúÿúúÿúúÿúúÿ");

            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
//            BufferedImage bi1 = ImageIO.read(bais);
            File w1=new File("C:\\Users\\16473\\Documents\\DSworks\\yzm.jpg");
            byte[] bytes2= FileUtils.readFileToByteArray(w1);
            File w2 = new File("D://jwcImg2.bmp");//可以是jpg,png,gif格式
            if(!w2.exists()){
                w2.createNewFile();
            }
//            ImageIO.write(bi1, "bmp", w2);//不管输出什么格式图片，此处不需改动
            FileOutputStream imageOutput=new FileOutputStream(w2);
            imageOutput.write(bytes2,0,bytes2.length);
            imageOutput.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
