package cn.wule.letter.filter;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 00:29

import cn.wule.letter.log.service.Impl.RequestLogService;
import cn.wule.letter.model.JwtUserInfo;
import cn.wule.letter.model.ResponseModel;
import cn.wule.letter.model.log.RequestLog;
import cn.wule.letter.model.user.User;
import cn.wule.letter.user.service.UserPermissionService;
import cn.wule.letter.util.JsonUtil;
import cn.wule.letter.util.JwtUtil;
import cn.wule.letter.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 *  对每个请求的JWT进行验证
 */
@Slf4j
@Component
public class RequestJwtFilter extends OncePerRequestFilter
{
    @Resource
    RedisUtil redisUtil;
    @Resource
    JsonUtil jsonUtil;
    @Resource
    JwtUtil jwtUtil;
    @Resource
    RequestLogService requestLogService;
    @Resource
    UserPermissionService userPermissionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //首先确认uri，直接跳过无需验证的路径
        String uri = request.getRequestURI();
        if(noVerify(uri)) {
            doFilter(request, response, filterChain);
            return;
        }
        //从请求头获取授权信息，如果没有则直接拒绝
        String strAuth = request.getHeader("Authorization");
        if (strAuth == null) {
            jsonUtil.writeStringJsonToResponse(response,"401","没有授权信息，请登录","");
            log.info("无授权信息登录，ip:{}，主机名:{}",request.getRemoteAddr(),request.getRemoteHost());
            requestLogService.insertLog(new RequestLog(null,request.getRemoteAddr(),request.getRemoteHost(),"401","没有授权信息，请登录",uri,null,null,null));
            return;
        }
        //获取JWT
        String jwt = strAuth.replace("bearer ", "");
        //判断jwt是否合法,合法则检查是否被吊销，不合法则直接拒绝
        JwtUserInfo userInfo = jwtUtil.verifyJWT(jwt);
        if(userInfo == null){
            jsonUtil.writeStringJsonToResponse(response,"401","授权信息不合法，请重新登录","");
            requestLogService.insertLog(new RequestLog(null,request.getRemoteAddr(),request.getRemoteHost(),"401","授权信息不合法，请重新登录",uri,null,null,null));
            return;
        }
        //判断redis中是否存在jwt缓存，没有则直接拒绝
        if(redisUtil.isJwtExist(userInfo)){
            //判断redis中的jwt是否与请求中的jwt一致，不一致则直接拒绝
            if(redisUtil.getJwt(userInfo).equals(jwt)){
                //获取用户权限信息，然后放入安全上下文。
                User user = User.builder().userId(userInfo.getUseId()).userName(userInfo.getUserName()).build();
                //获取用户权限信息
                List<SimpleGrantedAuthority> authList = userPermissionService.getUserPermissionByUserId(userInfo.getUseId()).stream().map(SimpleGrantedAuthority::new).toList();
                //注入用户权限信息,创建token
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, authList);
                //把token放到安全上下文里面
                SecurityContextHolder.getContext().setAuthentication(token);
                doFilter(request, response, filterChain);
                request.setAttribute("jwt", jwt);
                //记录日志
                requestLogService.insertLog(new RequestLog(null,request.getRemoteAddr(),request.getRemoteHost(),"200","请求成功",uri,userInfo.getUserName(), userInfo.getUseId(),null));
            }else {
                //jwt不一致，直接拒绝
                jsonUtil.writeStringJsonToResponse(response,"401","授权信息已失效，请重新登录","");
                requestLogService.insertLog(new RequestLog(null,request.getRemoteAddr(),request.getRemoteHost(),"401","授权信息已失效，请重新登录",uri,null,null,null));
            }
        }else {
            //redis中不存在jwt缓存，直接拒绝
            jsonUtil.writeStringJsonToResponse(response,"401","授权信息已失效，请重新登录","");
            requestLogService.insertLog(new RequestLog(null,request.getRemoteAddr(),request.getRemoteHost(),"401","授权信息已失效，请重新登录",uri,null,null,null));
        }
    }

    public boolean noVerify(String uri){
        return switch (uri) {
            case "/toLogin", "/signin", "/login/doLogin" -> true;
            default -> false;
        };
    }


}