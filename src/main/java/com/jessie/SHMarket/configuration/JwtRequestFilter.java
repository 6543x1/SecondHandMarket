package com.jessie.SHMarket.configuration;


import com.alibaba.fastjson.JSON;
import com.jessie.SHMarket.entity.Result;
import com.jessie.SHMarket.service.impl.UserDetailServiceImpl;
import com.jessie.SHMarket.utils.JwtTokenUtil;
import com.jessie.SHMarket.utils.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter
{
    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisUtil redisUtil;

    @Value("${jwt.route.authentication.path}")
    private String authenticationPath;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        final String requestTokenHeader = request.getHeader("token");
        String username = null;
        String jwtToken = null;
        //原本有Bearer前缀 现在我直接干掉了直接放不好吗
        if (requestTokenHeader != null)
        {
            jwtToken = requestTokenHeader;
            try
            {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                if (redisUtil.get("Jwt_TOKEN" + "|" + username) == null)
                {
                    throw new NullPointerException();
                } else
                {
                    System.out.println(redisUtil.get("Jwt_TOKEN" + "|" + username));
                }

            } catch (NullPointerException e)
            {
                System.out.println("token在服务器上不存在,可能是已经过期了");
                response.reset();
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().print(JSON.toJSONString(Result.error("服务器不存在此token，可能是过期了", 404)));
                return;
            } catch (IllegalArgumentException e)
            {
                System.out.println("token内容有误（？是这个吗）");
            } catch (ExpiredJwtException e)
            {
                System.out.println("JWT Token has expired");
                response.reset();
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().print(JSON.toJSONString(Result.error("Token过期了请重新登录获取新Token", 401)));
                return;
            }
        } else
        {
            logger.warn("JWT Token does not found");
        }
        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            UserDetails userDetails = this.userDetailServiceImpl.loadUserByUsername(username);
            // if token is valid configure Spring Security to manually set
            // authentication
            //这一步会比较jwt是否正确
            if (jwtTokenUtil.validateToken(jwtToken, userDetails))
            {
                //如果jwt中的username和SpringSecurity中记录的一致...
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                //这个filter有个很重要的一点是让springSecurity认到当前是谁
            }
        }//如果token不对springSecurity会认定为匿名用户
        //token不对就无法登录然后springSecurity中也没有相应信息，遇到相关请求就会报错
        chain.doFilter(request, response);
    }
}

