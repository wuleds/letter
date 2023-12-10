package cn.wule.letter.config;
//汉江师范学院 数计学院 吴乐创建于2023/11/17 17:17

import cn.wule.letter.model.JwtUserInfo;
import cn.wule.letter.model.ResponseModel;
import cn.wule.letter.model.user.User;
import cn.wule.letter.model.user.UserSecurity;
import cn.wule.letter.util.JsonUtil;
import cn.wule.letter.util.JwtUtil;
import cn.wule.letter.util.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 登录成功
 * @author wule
 */
@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private JsonUtil jsonUtil;
    @Resource
    private RedisUtil redisUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //从认证信息中获取用户信息
        UserSecurity userSecurity = (UserSecurity)authentication.getPrincipal();
        User user = userSecurity.getUser();
        //获取权限信息
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) userSecurity.getAuthorities();
        List<String> authList = authorities.stream().map(SimpleGrantedAuthority::getAuthority).toList();
        //生成jwt
        JwtUserInfo userInfo = JwtUserInfo.builder().userName(user.getUserName()).userId(user.getUserId()).build();
        //设置过期时间
        int expireDate = 1000 * 60 * 60 * 24 * 7;
        String jwt = jwtUtil.createJwtOnMouth(userInfo);
        //将jwt写入redis，设置过期时间
        redisUtil.addJwtRedisCache(jwt,expireDate);
        //将jwt写入响应
        jsonUtil.writeStringJsonToResponse(response,"200", "JWT生成成功",jwt);
    }
}