package com.jessie.SHMarket.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jessie.SHMarket.entity.GoodsImg;
import com.jessie.SHMarket.entity.Result;
import com.jessie.SHMarket.exception.WrongFileTypeException;
import com.jessie.SHMarket.service.GoodsImgService;
import com.jessie.SHMarket.service.GoodsService;
import com.jessie.SHMarket.service.UserService;
import com.jessie.SHMarket.utils.JwtTokenUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.jessie.SHMarket.controller.UserController.getCurrentUsername;

@RestController
@RequestMapping("/goods")
@SessionAttributes(value = {"username", "uid", "userPath", "resetCode", "mailAddr"}, types = {String.class, Integer.class, String.class, String.class, String.class})
public class GoodsImgController
{
    @Resource(name = "theImgSuffix")
    HashMap<Integer, String> theImgSuffix;
    @Autowired
    GoodsImgService goodsImgService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    UserService userService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    //删除图片和更改图片没写，只能建议有这个需要的。。。把商品删除重新上传吧
    //实在是懒得写
    @PostMapping(value = "/upload", produces = "text/html;charset=UTF-8")
    public String UploadById(@RequestParam("gid") int gid, @RequestParam("uploads") MultipartFile[] uploads, HttpServletRequest request) throws Exception
    {
        int uid;
        try
        {
            String token = request.getHeader("token");
            uid = jwtTokenUtil.getUidFromToken(token);
            if (uid != goodsService.getUid(gid))
            {
                return JSON.toJSONString("不是你的商品禁止操作", 403);
            }
        } catch (NullPointerException e)
        {
            return JSON.toJSONString(Result.error("没有token", 401));
        }

        System.out.println("通过gid文件上传开始...");
        String path = "/usr/tomcat/Img/" + getCurrentUsername();
        System.out.println(path);
        //如果文件重名，应该覆盖原文件吧（是否覆盖由前端决定）
        //选的是war exploded 那么文件会在工程目录下
        //否则在tomcat目录下
        int i = 0;
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
                if (!theImgSuffix.containsValue(suffix))
                {
                    throw new WrongFileTypeException("文件类型错误");
                }
                GoodsImg thisImage = new GoodsImg();
                thisImage.setName(gid + "_" + i + "." + suffix);
                i++;
                thisImage.setPath(path);
                thisImage.setUid(uid);
                thisImage.setGid(gid);
                goodsImgService.newImg(thisImage);
                System.out.println(thisImage.toString());
                upload.transferTo(new File(path, thisImage.getName()));
                System.out.println("文件保存成功，开始向数据库中更新文件夹数据");
            } catch (NullPointerException e)
            {
                e.printStackTrace();
                return JSON.toJSONString(Result.error("找不到文件的名字"));
            } catch (WrongFileTypeException e)
            {
                return JSON.toJSONString(Result.error("可能文件类型不对"));
            } catch (Exception e)
            {
                e.printStackTrace();
                return JSON.toJSONString(Result.error("未知错误"));
            }
        }
        goodsService.updateImgNum(gid, i);
        return JSON.toJSONString(Result.success("全部上传成功"));
    }

    @RequestMapping(value = "/getFirstPic", produces = "text/html;charset=UTF-8")
    public String downloadFile(HttpServletResponse response, int gid) throws Exception
    {
        List<GoodsImg> list = goodsImgService.getGoodsImg(gid);

        if (list == null)
        {
            return JSON.toJSONString(Result.error("文件不存在", 404));
        }

        try
        {
            //获取页面输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //读取文件
            byte[] bytes = FileUtils.readFileToByteArray(new File(list.get(0).getPath() + "/" + list.get(0).getName()));
            //向输出流写文件
            //写之前设置响应流以附件的形式打开返回值,这样可以保证前边打开文件出错时异常可以返回给前台
            response.setHeader("Content-Disposition", "attachment;filename=" + gid + list.get(0).getName().substring(list.get(0).getName().lastIndexOf(".")));
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e)
        {
            e.printStackTrace();
            return JSON.toJSONString(Result.error("服务器发生错误", 500));
        }
        return JSON.toJSONString(Result.success("开始下载"));
    }

    //感觉可以换用BASE64来上传图片
    @RequestMapping(value = "/downloadZip", produces = "text/html;charset=UTF-8")
    public ResponseEntity<byte[]> downloadZip(HttpServletRequest request, int gid) throws IOException
    {
        //需要压缩的文件
        List<GoodsImg> list = goodsImgService.getGoodsImg(gid);

        String path = list.get(0).getPath() + "/";
        //压缩后的文件
        String resourcesName = gid + ".zip";

        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(path + resourcesName));
        InputStream input = null;
        int uid = list.get(0).getUid();
        if (userService.getUser(uid).getStatus() <= 0)
        {
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
        File file = new File(path + resourcesName);
        HttpHeaders headers = new HttpHeaders();
        String filename = new String(resourcesName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/download", produces = "text/html;charset=UTF-8")
    public ResponseEntity<byte[]> download(HttpServletRequest request, int gid, int index) throws IOException
    {
        GoodsImg theGoodsImg = goodsImgService.getImgByIndex(index, gid);

        String path = theGoodsImg.getPath() + "/";
        //压缩后的文件

        int uid = theGoodsImg.getUid();
        if (userService.getUser(uid).getStatus() <= 0)
        {
            return null;
        }
        File file = new File(path + theGoodsImg.getName());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", theGoodsImg.getName());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/down", produces = "text/html;charset=UTF-8")
    public String download2(HttpServletRequest request, int gid) throws IOException
    {
        //需要压缩的文件
        List<GoodsImg> list = goodsImgService.getGoodsImg(gid);
        ArrayList<String> files = new ArrayList<>();
        String path = list.get(0).getPath() + "/";
        //压缩后的文件
        InputStream input = null;
        for (GoodsImg goodsImg : list)
        {
            String filePath = goodsImg.getPath() + "/" + goodsImg.getName();
            byte[] b = Files.readAllBytes(Paths.get(filePath));
            files.add(Base64.getEncoder().encodeToString(b));
            //System.out.println(Base64.getEncoder().encodeToString(b));
            //System.out.println("___________________________________");
        }
        return JSONArray.toJSONString(files);
    }
    @PostMapping(value = "/delete", produces = "text/html;charset=UTF-8")
    public String delete(HttpServletRequest request, String imgName) throws IOException
    {
        goodsImgService.deleteImg(imgName, jwtTokenUtil.getUidFromToken(request.getHeader("token")));
        return JSON.toJSONString(Result.success("删除成功"));
        //只是为了让这个项目的功能性稍微完整一些，因为前段并没有使用到这个功能
        //删除的话，倒是可以直接连着uid一起判断就好了
    }
}
