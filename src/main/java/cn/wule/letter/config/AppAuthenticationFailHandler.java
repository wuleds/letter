/*
package cn.wule.letter.config;
//汉江师范学院 数计学院 吴乐创建于2023/11/13 17:15

import cn.wule.requestsecurity.vo.HttpRequest;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

*/
/**
 * @author wule
 *//*

@Component
@Slf4j
public class AppAuthenticationFailHandler implements AuthenticationFailureHandler {
    @Resource
    private Gson gson;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        HttpRequest<String> httpRequest = HttpRequest.<String>builder()
                .code(0)
                .message("登录失败")
                .data("登录失败")
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