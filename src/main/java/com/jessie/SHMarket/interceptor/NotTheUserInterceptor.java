package com.jessie.SHMarket.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jessie.SHMarket.controller.UserController;
import com.jessie.SHMarket.entity.Result;
import com.jessie.SHMarket.entity.User;
import com.jessie.SHMarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * 自定义拦截器，可以用于拦截未登录情况下的操作
 */
@Component
public class NotTheUserInterceptor implements HandlerInterceptor
{
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        //将会在controller执行前拦截，true则放行
        System.out.println("拦截器1已经被执行。。。");
        int buyer= (int) request.getAttribute("uid");
        User theBuyer=userService.getUser(buyer);
        if (!theBuyer.getUsername().equals(UserController.getCurrentUsername()))
        {
            //重置response
            response.reset();
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter pw = response.getWriter();
            pw.write(objectMapper.writeValueAsString(Result.error("禁止非法调用别人账户", 403)));
            System.out.println("拦截了一个非法操作");
            //如果此处true，后面会提示该响应已经被执行了
            return false;
        }
        return true;
    }
}
