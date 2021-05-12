package com.jessie.SHMarket;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class TestDownloadYzm
{
    //下载图片
    @Test
    public void Download() {
        try {
            //开始时间
            //Date begindate = new Date();

                String imageName = "test.bmp";
                String theUrl="https://jwcjwxt2.fzu.edu.cn:82/plus/verifycode.asp?n=0.2682289971373786";
            URL uri = null;
            try
            {
                uri = new URL(theUrl);
            } catch (MalformedURLException malformedURLException)
            {
                malformedURLException.printStackTrace();
            }
            InputStream in = null;
            try
            {
                in = uri.openStream();
            } catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            FileOutputStream fo = null;//文件输出流
            try
            {
                fo = new FileOutputStream(new File("D:/"+imageName));

            } catch (FileNotFoundException fileNotFoundException)
            {
                fileNotFoundException.printStackTrace();
            }
            byte[] buf = new byte[1024];
                int length = 0;
                System.out.println("开始下载:" + theUrl);
                while (true) {
                    try
                    {
                        if (!((length = in.read(buf, 0, buf.length)) != -1)) break;
                    } catch (IOException ioException)
                    {
                        ioException.printStackTrace();
                    }
                    fo.write(buf, 0, length);
                }
                //关闭流
            try
            {
                in.close();
            } catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
            fo.close();
                System.out.println(imageName + "下载完成");
                //结束时间
        } catch (Exception e) {
            System.out.println("下载失败");
        }
    }
}
