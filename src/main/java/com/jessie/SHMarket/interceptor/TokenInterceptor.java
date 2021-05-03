package com.jessie.SHMarket.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.jessie.SHMarket.configuration.JwtTokenUtil;
import com.jessie.SHMarket.entity.Result;
import com.jessie.SHMarket.service.UserService;
import com.jessie.SHMarket.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor
{
    @Autowired
    UserService userService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception
    {
        System.out.println("token拦截器已经被执行");
        String token = request.getHeader("Authorization");
        if (null == token)
        {
            System.out.println("NULL TOKEN");
            response.reset();
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setStatus(403);
            response.getWriter().write(JSONObject.toJSONString(Result.error("NULL TOKEN", 403)));
            return false;
        }
        try
        {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(username);
            if (!jwtTokenUtil.validateToken(token, userDetails))
            {
                System.out.println("TOKEN expired");
                response.reset();
                //设置编码格式
                response.setCharacterEncoding("UTF-8");
                response.setStatus(403);
                response.getWriter().write(JSONObject.toJSONString(Result.error("token expired or fake", 403)));
                return false;
            }
            System.out.println("验证token成功");
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("验证过程发现错误");
            //重置response
            response.reset();
            //设置编码格式
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(Result.error("token expired or fake", 403)));
            response.setStatus(500);
            return false;
        }
        return true;
    }
}
