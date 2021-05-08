package com.jessie.SHMarket.controller;

import com.alibaba.fastjson.JSON;
import com.jessie.SHMarket.configuration.JwtTokenUtil;
import com.jessie.SHMarket.configuration.RedisUtil;
import com.jessie.SHMarket.entity.JwtRequest;
import com.jessie.SHMarket.entity.JwtResponse;
import com.jessie.SHMarket.entity.Result;
import com.jessie.SHMarket.entity.User;
import com.jessie.SHMarket.exception.BannedUserException;
import com.jessie.SHMarket.service.UserService;
import com.jessie.SHMarket.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * JwtAuthenticationController
 * 包含登陆和查看token的方法
 *
 * @author zhengkai.blog.csdn.net
 */
@RestController
@CrossOrigin
public class JwtAuthenticationController
{
    @Autowired
    UserService userService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Value("${jwt.header}")
    private String tokenHeader;

    @PostMapping(value = "${jwt.route.authentication.path}", produces = "application/json;charset=UTF-8")
    public String createAuthenticationToken(JwtRequest authenticationRequest) throws Exception
    {
        System.out.println("username:" + authenticationRequest.getUsername() + ",password:" + authenticationRequest.getPassword());
        try
        {
            if (userService.getUser(authenticationRequest.getUsername()).getStatus() <= 0)
            {
                throw new BannedUserException();
            }
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        } catch (BannedUserException e)
        {
            return JSON.toJSONString(Result.error("账号被封禁了，有疑问联系管理员", 403));
        } catch (Exception e)
        {

            return JSON.toJSONString(Result.error("账号或密码错误", 401));
        }
        final UserDetails userDetails = userDetailService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails, userService.getUser(userDetails.getUsername()).getUid());//这一步生成token并返回
        redisUtil.set("Jwt_TOKEN" + "|" + authenticationRequest.getUsername(), token, 72 * 60 * 60);
        return JSON.toJSONString(Result.success("loginSuccess", new JwtResponse(token, userService.getUser(authenticationRequest.getUsername()))));
    }

    private void authenticate(String username, String password) throws Exception
    {
        try
        {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e)
        {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e)
        {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @GetMapping(value = "/token", produces = "application/json;charset=UTF-8")
    public User getAuthenticatedUser(HttpServletRequest request)
    {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        System.out.println("username=" + username);
        System.out.println(jwtTokenUtil.getUidFromToken(token));
        return userService.getUser(username);
    }

}

