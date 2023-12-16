package cn.wule.letter.authTest.controller;
//汉江师范学院 数计学院 吴乐创建于2023/12/16 19:10

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/test")
public class Info
{
    /**方式一*/
    @GetMapping("/authentication")
    public Authentication login(Authentication authentication)
    {
        return authentication;
    }

    /**方式二*/
    @GetMapping("/principal")
    public Principal login(Principal principal)
    {
        System.out.println(principal);
        return principal;
    }

    /**方式三*/
    @GetMapping("/securityContextHolder")
    public Principal getData()
    {
        //获取安全上下文，再获取认证信息
        return SecurityContextHolder.getContext().getAuthentication();
    }
}