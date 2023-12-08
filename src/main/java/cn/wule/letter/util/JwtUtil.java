package cn.wule.letter.util;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 00:24

import cn.wule.letter.model.JwtUserInfo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jwt工具类
 */
@Slf4j
@Component
public class JwtUtil
{
    @Value("${jwt.secretKey}")
    String secretKey;
    @Value("${jwt.issuer}")
    String issuer;

    //生成JWT
    public String createJwt(JwtUserInfo userInfo, Date issueDate, Date expireDate){
        Map<String,Object> headerClaims = new HashMap<>();
        //加密算法
        headerClaims.put("alg","HS256");
        headerClaims.put("typ","JWT");
        JWTCreator.Builder builder = JWT.create();
        builder.withHeader(headerClaims)
                //签名生成者
                .withIssuer(issuer)
                //生成签名的时间
                .withIssuedAt(issueDate)
                //签名过期的时间
                .withExpiresAt(expireDate)
                //自定义
                .withClaim("userId",userInfo.getUseId())
                .withClaim("userName",userInfo.getUserName())
                //添加自定义用户信息
                .withClaim("userInfo",userInfo.getUserInfo());

     /*   if(userInfo.getUserInfo() != null){
            if(!userInfo.getUserInfo().isEmpty()) {
                for (Map.Entry<String, String> info : userInfo.getUserInfo().entrySet()) {
                    builder.withClaim(info.getKey(), info.getValue());
                }
            }
        }*/
        String jwt = builder.sign(Algorithm.HMAC256(secretKey));
        log.info("jwt:用户ID：{}，用户名:{}，{}",userInfo.getUserName(),userInfo.getUseId(),jwt);
        return jwt;
    }
    //验证JWT,并获取用户信息
    public JwtUserInfo verifyJWT(String jwt){
        JwtUserInfo jwtUserInfo = new JwtUserInfo();
        DecodedJWT decodedJWT;
        try{
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
            decodedJWT = jwtVerifier.verify(jwt);
        }catch (Exception e){
            log.error("util.JwtUtil.verifyJWT() error:{}",e.getMessage());
            return null;
        }
        String userId = decodedJWT.getClaim("userId").asString();
        String userName = decodedJWT.getClaim("userName").asString();
        Map<String ,Object > userInfoObj =  decodedJWT.getClaim("userInfo").asMap();

        if(userInfoObj != null) {
            Map<String, String> userInfo = new HashMap<>();
            for (Map.Entry<String, Object> entry : userInfoObj.entrySet()) {
                userInfo.put(entry.getKey(), entry.getValue().toString());
            }
            jwtUserInfo.setUserInfo(userInfo);
        }
        jwtUserInfo.setUseId(userId);
        jwtUserInfo.setUserName(userName);
        return jwtUserInfo;
    }
}