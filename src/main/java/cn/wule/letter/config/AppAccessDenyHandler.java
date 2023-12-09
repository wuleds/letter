package cn.wule.letter.config;
//汉江师范学院 数计学院 吴乐创建于2023/11/13 17:24

import cn.wule.letter.util.JsonUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义拒绝访问处理器
 */
@Component
@Slf4j
public class AppAccessDenyHandler implements AccessDeniedHandler
{
    @Resource
    private JsonUtil jsonUtil;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        jsonUtil.writeStringJsonToResponse(response,"403","拒绝访问",null);
    }
}