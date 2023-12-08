/*
package cn.wule.letter.config;
//汉江师范学院 数计学院 吴乐创建于2023/11/13 17:20

import cn.wule.requestsecurity.vo.HttpRequest;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

*/
/**
 * @author wule
 *//*

@Component
@Slf4j
public class AppLogoutSuccessHandler implements LogoutSuccessHandler
{
    @Resource
    private Gson gson;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpRequest<String> httpRequest = HttpRequest.<String>builder()
                .code(1)
                .message("退出成功")
                .build();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(gson.toJson(httpRequest));
        printWriter.flush();
        printWriter.close();
    }
}*/