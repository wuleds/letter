package cn.wule.letter.config;
//汉江师范学院 数计学院 吴乐创建于2023/11/22 0:09

import cn.wule.letter.log.service.LogoutService;
import cn.wule.letter.model.JwtUserInfo;
import cn.wule.letter.model.log.LogoutLog;
import cn.wule.letter.util.JsonUtil;
import cn.wule.letter.util.JwtUtil;
import cn.wule.letter.util.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 注销登录的操作
 * @author wule
 */
@Component
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler
{
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private JsonUtil jsonUtil;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private LogoutService logoutService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //获取授权信息
        String auth = request.getHeader("Authorization");
        if(auth == null){
           jsonUtil.writeStringJsonToResponse(response,"401","token为空","");
           logoutService.insertLog(new LogoutLog("",request.getRemoteAddr(),
                   request.getRemoteHost(),"401","token为空","","",""));
            return;
        }
        //获取jwt
        String jwt = auth.replace("bearer ", "");
        JwtUserInfo userInfo = jwtUtil.verifyJWT(jwt);
        if(userInfo == null){
            jsonUtil.writeStringJsonToResponse(response,"401","token非法","");
            logoutService.insertLog(new LogoutLog("",request.getRemoteAddr(),
                    request.getRemoteHost(),"401","token非法","","",""));
            return;
        }
        //从redis中删除缓存，使之失效
        redisUtil.deleteJwtRedisCache(jwt);
        //要求前端登出
        jsonUtil.writeStringJsonToResponse(response,"200","注销成功,跳转登录界面","");
        logoutService.insertLog(new LogoutLog(userInfo.getUserName(),request.getRemoteAddr(),
                request.getRemoteHost(),"200","注销成功,跳转登录界面",userInfo.getUserName(), userInfo.getUseId(), ""));
    }
}