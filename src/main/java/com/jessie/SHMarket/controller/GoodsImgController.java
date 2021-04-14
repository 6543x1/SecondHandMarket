package com.jessie.SHMarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jessie.SHMarket.entity.GoodsImg;
import com.jessie.SHMarket.entity.Result;
import com.jessie.SHMarket.entity.User;
import com.jessie.SHMarket.service.GoodsImgService;
import com.jessie.SHMarket.service.UserService;
import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/goods")
@SessionAttributes(value = {"username", "uid", "userPath", "resetCode", "mailAddr"}, types = {String.class, Integer.class, String.class, String.class, String.class})
public class GoodsImgController
{
    @Autowired
    ObjectMapper objectMapper;
    @Resource(name="theImgSuffix")
    HashMap<Integer,String> theImgSuffix;
    @Autowired
    GoodsImgService goodsImgService;
    @Autowired
    UserService userService;
    @PostMapping(value = "/upload", produces = "text/html;charset=UTF-8")
    public String UploadById (HttpServletRequest request, @RequestParam("gid") int gid, @RequestParam("uploads") MultipartFile[] uploads, ModelMap modelmap) throws Exception
    {
        if (modelmap.get("uid") == null)
        {
            System.out.println("还没有登录");
            return "请先登入";
        }
        System.out.println("通过gid文件上传开始...");
        int uid=(Integer)modelmap.get("uid");

        String path = "D:/SHMarket/"+modelmap.get("username");
        System.out.println(path);
        //如果文件重名，应该覆盖原文件吧（是否覆盖由前端决定）
        //选的是war exploded 那么文件会在工程目录下
        //否则在tomcat目录下
        for(MultipartFile upload:uploads)
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
                if (!theImgSuffix.containsValue(suffix))
                {
                    throw new WrongNumberArgsException("?");
                }
                GoodsImg thisImage = new GoodsImg();
                thisImage.setName(UUID.randomUUID().toString().replace("-","")+"."+suffix);
                thisImage.setPath(path);
                thisImage.setUid(uid);
                thisImage.setGid(gid);
                goodsImgService.newImg(thisImage);
                System.out.println(thisImage.toString());
                upload.transferTo(new File(path, thisImage.getName()));
                System.out.println("文件保存成功，开始向数据库中更新文件夹数据");

            } catch (WrongNumberArgsException e)
            {
                return objectMapper.writeValueAsString(Result.error("文件类型不对"));
            } catch (NullPointerException e)
            {
                e.printStackTrace();
                return objectMapper.writeValueAsString(Result.error("找不到文件的名字"));
            } catch (Exception e)
            {
                e.printStackTrace();
                return objectMapper.writeValueAsString(Result.error("未知错误"));
            }
        }
        return objectMapper.writeValueAsString(Result.success("全部上传成功"));
    }
    @RequestMapping(value = "/getFirstPic", produces = "text/html;charset=UTF-8")
    public String downloadFile(HttpServletResponse response, int gid, ModelMap modelMap) throws Exception
    {
        List<GoodsImg> list=goodsImgService.getGoodsImg(gid);

        if (list == null)
        {
            return objectMapper.writeValueAsString(Result.error("文件不存在", 404));
        }

        try
        {
            //获取页面输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //读取文件
            byte[] bytes = FileUtils.readFileToByteArray(new File(list.get(0).getPath() + list.get(0).getName()));
            //向输出流写文件
            //写之前设置响应流以附件的形式打开返回值,这样可以保证前边打开文件出错时异常可以返回给前台
            response.setHeader("Content-Disposition", "attachment;filename=" + list.get(0).getName());
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();

        } catch (IOException e)
        {
            e.printStackTrace();
            return objectMapper.writeValueAsString(Result.error("服务器发生错误", 500));
        }
        return objectMapper.writeValueAsString(Result.success("开始下载"));
    }
    //感觉可以换用BASE64来上传图片
    @RequestMapping  (value = "/download", produces = "text/html;charset=UTF-8")
    public ResponseEntity<byte[]> download2(HttpServletRequest request,int gid,ModelMap modelMap) throws IOException {
        //需要压缩的文件
        List<GoodsImg> list=goodsImgService.getGoodsImg(gid);

        String path=list.get(0).getPath()+"/";
        //压缩后的文件
        String resourcesName = gid+".zip";

        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(path+resourcesName));
        InputStream input = null;
        int uid=list.get(0).getUid();
        if(userService.getUser(uid).getStatus()>3){
            return null;
        }
        for (GoodsImg goodsImg : list) {
            String name = goodsImg.getPath()+"/"+goodsImg.getName();
            input = new FileInputStream(new File(name));
            zipOut.putNextEntry(new ZipEntry(goodsImg.getName()));
            int temp = 0;
            while((temp = input.read()) != -1){
                zipOut.write(temp);
            }
            input.close();
        }
        zipOut.close();
        File file = new File(path+resourcesName);
        HttpHeaders headers = new HttpHeaders();
        String filename = new String(resourcesName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.CREATED);
    }
}
