package com.jessie.SHMarket.service.impl;

import com.jessie.SHMarket.dao.GoodsImgDAO;
import com.jessie.SHMarket.entity.GoodsImg;
import com.jessie.SHMarket.service.GoodsImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service("goodsImgService")
public class GoodsImgServiceImpl implements GoodsImgService
{
    @Autowired
    GoodsImgDAO goodsImgDAO;
    @Resource(description = "theImgSuffix")
    HashMap<Integer, String> theImgSuffix;

    @Override
    public void newImg(GoodsImg goodsImg)
    {
        goodsImgDAO.newImg(goodsImg);
    }

    @Override
    public List<GoodsImg> getGoodsImg(int gid)
    {
        return goodsImgDAO.getGoodsImg(gid);
    }

    public static String saveImg(int uid, MultipartFile[] uploads, String username)
    {
        String path = "/usr/tomcat/Img/" + username;
        System.out.println(path);
        //如果文件重名，应该覆盖原文件吧（是否覆盖由前端决定）
        //选的是war exploded 那么文件会在工程目录下
        //否则在tomcat目录下
        for (MultipartFile upload : uploads)
        {
            File file = new File(path);
            if (!file.exists())
            {
                file.mkdirs();
            }
            try
            {
                String filename = upload.getOriginalFilename();
                String suffix = filename.substring(filename.lastIndexOf(".") + 1);

                upload.transferTo(new File(path, UUID.randomUUID().toString().replace("-", "") + "." + suffix));
                System.out.println("文件保存成功");

            } catch (NullPointerException e)
            {
                e.printStackTrace();
                return "找不到文件名字";
            } catch (RuntimeException e)
            {
                return "可能文件类型不对";
            } catch (Exception e)
            {
                e.printStackTrace();
                return "未知错误";
            }
        }
        return "全部保存成功";
    }

    @Override
    public GoodsImg getImgByIndex(int index, int gid)
    {
        return goodsImgDAO.getImgByIndex(index, gid);
    }
}
