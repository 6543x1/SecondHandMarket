package com.jessie.SHMarket;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.CookieStore;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestDownloadYzm
{
    private static Map<String, String> cookies = null;
    private  String viewState = null;
    public static void main(String[] args){
        try
        {
            getLogInfo();
            login();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Test
    public static void getLogInfo() throws Exception {
        String urlLogin = "https://jwcjwxt2.fzu.edu.cn:82/plus/verifycode.asp";
        Connection connect = Jsoup.connect(urlLogin);
        // 伪造请求头
        connect.header("Accept", "image/webp,*/*").header("Accept-Encoding",
                "gzip, deflate,br");
        connect.header("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2").header("Connection", "keep-alive");
        connect.header("Content-Length", "213").header("Content-Type",
                "application/x-www-form-urlencoded; charset=UTF-8");
        connect.header("Host", "jwcjwxt2.fzu.edu.cn:82").header("Referer", "https://jwch.fzu.edu.cn/html/login/1.html");
        connect.header("User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        // 请求url获取响应信息
        Connection.Response res = connect.ignoreContentType(true).method(Connection.Method.POST).execute();// 执行请求
        // 获取返回的cookie
        cookies = res.cookies();
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            System.out.println(entry.getKey() + "-" + entry.getValue());
        }
        //拿到cookie了！！！
        // 获取响应体
        String body = res.body();
        System.out.println(body);
        byte[] bytes= res.bodyAsBytes();
        File file=new File("D://imageYzm.png");
        if(!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fileOutputStream=new FileOutputStream(file);
        fileOutputStream.write(bytes);

        // 调用下面方法获取__viewstate
        //this.getViewState(body);// 获取viewState
        //调用下载验证码的工具类下载验证码
        //JsoupDoloadPicture.downloadImg("http://newjwc.tyust.edu.cn/CheckCode.aspx", cookies);;
    }
    @Test
    public static void login() throws Exception {
        String No="031902410";
        String Password="123456cnm";
        String urlLogin = "https://jwcjwxt2.fzu.edu.cn:82/logincheck.asp";
        Connection connect = Jsoup.connect(urlLogin);
        connect.timeout(5 * 100000).followRedirects(false);
        // 伪造请求头
        connect.header("Accept", "image/webp,*/*").header("Accept-Encoding",
                "gzip, deflate,br");
        connect.header("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2").header("Connection", "keep-alive");
        connect.header("Content-Length", "213").header("Content-Type",
                "application/x-www-form-urlencoded; charset=UTF-8");
        connect.header("Host", "jwcjwxt2.fzu.edu.cn:82").header("Referer", "https://jwch.fzu.edu.cn/html/login/1.html");
        connect.header("User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        // 输入验证码
        System.out.println("-----------请输入验证码---------");
        Scanner sc = new Scanner(System.in);
        String yzm = sc.next();
        sc.close();
        // 携带登陆信息
        connect.data("muser",No).data("passwd",Password).data("Verifycode",yzm);
        connect.cookies(cookies);
        // 请求url获取响应信息
        Connection.Response res = connect.ignoreContentType(true).method(Connection.Method.POST).execute();// 执行请求
        // 获取返回的cookie
        cookies = res.cookies();
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            System.out.println(entry.getKey() + "-" + entry.getValue());
        }
        System.out.println("---------获取的登录之后的页面-----------");
        String body = res.body();// 获取响应体
        System.out.println(body);
        System.out.println(res.statusCode());
        if(body.contains("<!--include file=\"check.asp\" -->")){
            System.out.println("ERROR");
        }
    }
    /**
     * 获取viewstate
     *
     * @return
     */


    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public String getViewState() {
        return viewState;
    }

    //下载图片

//    public void Download() {
//        try {
//            //开始时间
//            //Date begindate = new Date();
//
//                String imageName = "test.bmp";
//                String theUrl="https://jwcjwxt2.fzu.edu.cn:82/plus/verifycode.asp?n=0.2682289971373786";
//            URL uri = null;
//            try
//            {
//                uri = new URL(theUrl);
//            } catch (MalformedURLException malformedURLException)
//            {
//                malformedURLException.printStackTrace();
//            }
//            InputStream in = null;
//            try
//            {
//                in = uri.openStream();
//            } catch (IOException ioException)
//            {
//                ioException.printStackTrace();
//            }
//            FileOutputStream fo = null;//文件输出流
//            try
//            {
//                fo = new FileOutputStream(new File("D:/"+imageName));
//
//            } catch (FileNotFoundException fileNotFoundException)
//            {
//                fileNotFoundException.printStackTrace();
//            }
//            byte[] buf = new byte[1024];
//                int length = 0;
//                System.out.println("开始下载:" + theUrl);
//                while (true) {
//                    try
//                    {
//                        if (!((length = in.read(buf, 0, buf.length)) != -1)) break;
//                    } catch (IOException ioException)
//                    {
//                        ioException.printStackTrace();
//                    }
//                    fo.write(buf, 0, length);
//                }
//                //关闭流
//            try
//            {
//                in.close();
//            } catch (IOException ioException)
//            {
//                ioException.printStackTrace();
//            }
//            fo.close();
//                System.out.println(imageName + "下载完成");
//                //结束时间
//        } catch (Exception e) {
//            System.out.println("下载失败");
//        }
//    }
//
//    public void testJWCHPost() throws Exception
//    {
//        String No="03192410";
//        String Password="123456cnm";
//        int code = 200;
//        String cookieUrl="https://jwch.fzu.edu.cn/html/login/1.html";
//        GetMethod getMethod=new GetMethod(cookieUrl);
//        org.apache.commons.httpclient.HttpClient httpClient2 = new org.apache.commons.httpclient.HttpClient();
//        int status=httpClient2.executeMethod(getMethod);
//
//
//        String result;
//        try
//        {
//            String postURL = "https://jwcjwxt2.fzu.edu.cn:82/logincheck.asp";
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
//
//            NameValuePair[] data = {
//                    new NameValuePair("muser", No),
//                    new NameValuePair("passwd", Password),
//                    new NameValuePair("VerifyCode","123")
//
//            };
//            //参数设置，需要注意的就是里边不能传NULL，要传空字符串
//            postMethod.setRequestBody(data);
//            org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
//            int response = httpClient.executeMethod(postMethod); // 执行POST方法
//            result = postMethod.getResponseBodyAsString();
//            System.out.println(result);
//            System.out.println(postMethod.getStatusCode());
//            code = postMethod.getStatusCode();
//            postMethod.abort();
//        } catch (Exception e)
//        {
//            System.out.println("请求异常" + e.getMessage());
//            throw new RuntimeException(e.getMessage());
//        }
//        if (code == 302)
//        {
//            return;
//        }
//        return;
//    }
}
