package cn.wule.letter.util;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 00:24

import cn.wule.letter.model.JwtUserInfo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
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

    /**创建一个有效期为一个月的jwt*/
    public String createJwtOnMouth(JwtUserInfo jwtUserInfo){
        long expireTime = 1000L * 60 * 60 * 24 * 30;
        return createJwt(jwtUserInfo,new Date(),new Date(System.currentTimeMillis() + expireTime));
    }
    /**生成JWT*/
    public String createJwt(JwtUserInfo jwtUserInfo, Date issueDate, Date expireDate){
        Map<String,Object> headerClaims = new HashMap<>();
        //加密算法
        headerClaims.put("alg","HMAC256");
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
                .withClaim("userId",jwtUserInfo.getUserId())
                .withClaim("userName",jwtUserInfo.getUserName())
                //添加自定义用户信息
                .withClaim("userInfo",jwtUserInfo.getUserInfo());

     /*   if(userInfo.getUserInfo() != null){
            if(!userInfo.getUserInfo().isEmpty()) {
                for (Map.Entry<String, String> info : userInfo.getUserInfo().entrySet()) {
                    builder.withClaim(info.getKey(), info.getValue());
                }
            }
        }*/
        return builder.sign(Algorithm.HMAC256(secretKey));
    }

    /**验证JWT,并获取用户信息*/
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
        jwtUserInfo.setUserId(userId);
        jwtUserInfo.setUserName(userName);
        return jwtUserInfo;
    }
}