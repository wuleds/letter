//package cn.wule.letter.config;
////汉江师范学院 数计学院 吴乐创建于2023/11/17 17:17
//
//import cn.wule.requestsecurity.Utils.JWTTest;
//import cn.wule.requestsecurity.model.User;
//import cn.wule.requestsecurity.vo.HttpRequest;
//import cn.wule.requestsecurity.vo.JwtUserInfo;
//import cn.wule.requestsecurity.vo.UserSecurity;
//import com.google.gson.Gson;
//import jakarta.annotation.Resource;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author wule
// */
//@Component
//@Slf4j
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler
//{
//    @Resource
//    private Gson gson;
//    @Resource
//    private JWTTest jwtUtil;
//    @Resource
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        //从认证信息中获取用户信息
//        UserSecurity userSecurity = (UserSecurity)authentication.getPrincipal();
//        User user = userSecurity.getUser();
//        //获取权限信息
//        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) userSecurity.getAuthorities();
//        List<String> authList = authorities.stream().map(SimpleGrantedAuthority::getAuthority).toList();
//        //生成jwt
//        JwtUserInfo userInfo = JwtUserInfo.builder().userName(user.getUserName()).useId(user.getUserId()).authList(authList).build();
//        int issDate = 1000 * 60 * 60 * 24;
//        String jwt = jwtUtil.createJWT(userInfo, new Date(), new Date(System.currentTimeMillis() + issDate));
//        HttpRequest<String> httpRequest = HttpRequest.<String>builder()
//                .code(200)
//                .message("生成jwt")
//                .data(jwt)
//                .build();
//        //将jwt写入redis，设置过期时间
//        stringRedisTemplate.opsForValue().set("auth:"+jwt,gson.toJson(authentication),issDate, TimeUnit.MILLISECONDS);
//        //将jwt写入响应
//        writeJson(request,response,authentication.getName(),jwt);
//    }
//
//    private void writeJson(HttpServletRequest request,HttpServletResponse response,String id,String jwt) throws IOException {
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json;charset=utf-8");
//        //写入授权头，前端要取出来存储，下次请求时带上
//        response.setHeader("Authorization","bearer "+ jwt);
//
//        //设置Cookie，前端请求自动带上
//        Cookie cookie = new Cookie("jwt-" + id, jwt);
//        cookie.setPath("/");
//        response.addCookie(cookie);
//    }
//}