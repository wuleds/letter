package cn.wule.letter.authTest.controller;
//汉江师范学院 数计学院 吴乐创建于2023/12/16 19:10

import cn.wule.letter.model.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/test")
@Slf4j
public class Info
{
    /**方式二*/
    @GetMapping("/principal")
    public Principal login(Principal principal)
    {
        System.out.println(principal);
        return principal;
    }

    /**方式三*/
    @GetMapping("/securityContextHolder")
    public Principal getData() throws JsonProcessingException {
        //获取安全上下文，再获取认证信息
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(user.toString());
        return SecurityContextHolder.getContext().getAuthentication();
    }
}