package com.jessie.SHMarket;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@SpringBootTest
class ShmarketApplicationTests
{

    @Test
    void contextLoads()
    {
    }
    @Test
    void getEncodedPassword(){
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("123456"));
        System.out.println(bCryptPasswordEncoder.encode("123"));
        //System.out.println(DigestUtils.md5DigestAsHex(("123"+"b040fe39-be75-472e-b751-925046be03e9").getBytes(StandardCharsets.UTF_8)));
    }

}
