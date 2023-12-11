package cn.wule.letter.filter;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 00:29

import cn.wule.letter.log.service.Impl.RequestLogServiceImpl;
import cn.wule.letter.model.JwtUserInfo;
import cn.wule.letter.model.log.RequestLog;
import cn.wule.letter.model.user.User;
import cn.wule.letter.user.service.UserPermissionService;
import cn.wule.letter.util.JsonUtil;
import cn.wule.letter.util.JwtUtil;
import cn.wule.letter.util.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
    RequestLogServiceImpl requestLogServiceImpl;
    @Resource
    UserPermissionService userPermissionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //首先确认uri，直接跳过无需验证的路径
        String uri = request.getRequestURI();
        RequestLog requestLog = RequestLog.builder()
                .ip(request.getRemoteAddr())
                .host(request.getRemoteHost())
                .uri(uri)
                .code("")
                .msg("")
                .userName("")
                .userId("")
                .userInfo("")
                .build();

        if(noVerify(uri)) {
            log.info("无需拦截，ip:{}，主机名:{}",request.getRemoteAddr(),request.getRemoteHost());
            doFilter(request, response, filterChain);
            requestLog.setCode("200");
            requestLog.setMsg("无需拦截");
            requestLogServiceImpl.insertLog(requestLog);
            return;
        }
        //从请求头获取授权信息，如果没有则直接拒绝
        String strAuth = request.getHeader("Authorization");
        if (strAuth == null) {
            jsonUtil.writeStringJsonToResponse(response,"401","没有授权信息，请登录","");
            log.info("无授权信息登录，uri:{},ip:{}，主机名:{}",uri,request.getRemoteAddr(),request.getRemoteHost());
            requestLog.setCode("401");
            requestLog.setMsg("没有授权信息，请登录");
            requestLogServiceImpl.insertLog(requestLog);
            return;
        }
        //获取JWT
        String jwt = strAuth.replace("bearer ", "");
        //判断jwt是否合法,合法则检查是否被吊销，不合法则直接拒绝
        JwtUserInfo userInfo = jwtUtil.verifyJWT(jwt);
        if(userInfo == null){
            jsonUtil.writeStringJsonToResponse(response,"401","授权信息不合法，请重新登录","");
            requestLog.setCode("401");
            requestLog.setMsg("授权信息不合法，请重新登录");
            requestLogServiceImpl.insertLog(requestLog);
            return;
        }
        //判断redis中是否存在jwt缓存，没有则直接拒绝
        if(redisUtil.isJwtExist(userInfo)){
            //判断redis中的jwt是否与请求中的jwt一致，不一致则直接拒绝
            if(redisUtil.getJwt(userInfo).equals(jwt)){
                //jwt一致，放行
                //获取用户权限信息，然后放入安全上下文。
                User user = User.builder().userId(userInfo.getUserId()).userName(userInfo.getUserName()).build();
                //获取用户权限信息
                List<SimpleGrantedAuthority> authList = userPermissionService.getUserPermissionByUserId(userInfo.getUserId()).stream().map(SimpleGrantedAuthority::new).toList();
                //注入用户权限信息,创建token
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, "", authList);
                //把token放到安全上下文里面
                SecurityContextHolder.getContext().setAuthentication(token);
                doFilter(request, response, filterChain);
                request.setAttribute("jwt", jwt);
                //记录日志
                requestLog.setCode("200");
                requestLog.setMsg("请求成功");
                requestLog.setUserId(userInfo.getUserId());
                requestLog.setUserName(userInfo.getUserName());
                requestLogServiceImpl.insertLog(requestLog);
            }else {
                //jwt不一致，直接拒绝
                jsonUtil.writeStringJsonToResponse(response,"401","授权信息已失效，请重新登录","");
                requestLog.setCode("401");
                requestLog.setMsg("授权信息已失效，请重新登录");
                requestLog.setUserId(userInfo.getUserId());
                requestLog.setUserName(userInfo.getUserName());
                requestLogServiceImpl.insertLog(requestLog);
            }
        }else {
            //redis中不存在jwt缓存，直接拒绝
            jsonUtil.writeStringJsonToResponse(response,"401","授权信息已失效，请重新登录","");
            requestLog.setCode("401");
            requestLog.setMsg("授权信息已失效，请重新登录");
            requestLog.setUserId(userInfo.getUserId());
            requestLog.setUserName(userInfo.getUserName());
            requestLogServiceImpl.insertLog(requestLog);
        }
    }

    public boolean noVerify(String uri){
        return switch (uri) {
            case "/forget", "/user/signin", "/user/login" -> true;
            default -> false;
        };
    }


}