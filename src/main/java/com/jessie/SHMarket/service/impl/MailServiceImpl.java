package com.jessie.SHMarket.service.impl;

import com.jessie.SHMarket.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;

@Service("mailService")
public class MailServiceImpl implements MailService
{
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Resource(name = "findPwTemplate")
    private SimpleMailMessage findPwTemplate;
    @Resource(name = "newOrderTemplate")
    private SimpleMailMessage newOrderTemplate;
    @Override
    public void sendResetPw(String dest, String theInfo)
    {
        if (dest == null)
        {
            System.out.println("邮箱地址为空");
            return;
        }
        SimpleMailMessage msg = new SimpleMailMessage(findPwTemplate);
        msg.setTo(dest);//接收人
        msg.setText(theInfo);  //这里的邮件内容是 文本类型
        // msg.setCc(cc);// 抄送
        // msg.setBcc(bcc);// 密送
        // msg.setReplyTo(replyTo);// 回复
        // msg.setSentDate(new Date());// 发送时间
        // msg.setSubject(subject);// 主题
        // msg.setFrom(from);// 发送人
        try
        {
            this.mailSender.send(msg);
        } catch (MailException ex)
        {
            System.out.println(ex.getMessage());
        }

    }
    @Override
    public void sendNewOrder(String dest, String theInfo)
    {
        if (dest == null)
        {
            System.out.println("邮箱地址为空");
            return;
        }
        SimpleMailMessage msg = new SimpleMailMessage(this.newOrderTemplate);
        msg.setTo(dest);//接收人
        msg.setText(theInfo);  //这里的邮件内容是 文本类型
        try
        {
            this.mailSender.send(msg);
        } catch (MailException ex)
        {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void newMessage(String subject, String dest, String theInfo)
    {
        if (dest == null)
        {
            System.out.println("邮箱地址为空");
            return;
        }
        SimpleMailMessage msg = new SimpleMailMessage(this.findPwTemplate);
        msg.setSubject(subject);
        msg.setTo(dest);
        msg.setText(theInfo);
        try
        {
            this.mailSender.send(msg);
        } catch (MailException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public static String getRandomString()
    {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < 6; i++)
        {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum))
            {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum))//额copy过来的时候报错，既然工作正常就不改了吧
            { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
