/*
package cn.wule.letter.config;
//汉江师范学院 数计学院 吴乐创建于2023/11/13 16:31

import cn.wule.requestsecurity.vo.HttpRequest;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

*/
/**
 * @author wule
 *//*

@Component
@Slf4j
public class AppAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private Gson gson;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpRequest<Authentication> httpRequest = HttpRequest.<Authentication>builder()
                .code(1)
                .message("登录成功")
                .data(authentication)
                .build();
        String json = gson.toJson(httpRequest);
        log.info(json);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(json);
        printWriter.flush();
        printWriter.close();
    }
}*/