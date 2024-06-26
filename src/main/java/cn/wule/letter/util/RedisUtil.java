package cn.wule.letter.util;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 01:47

import cn.wule.letter.model.JwtUserInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisUtil
{
    @Resource(name = "stringRedisTemplate")
    StringRedisTemplate redisTemplate;
    @Resource
    JwtUtil jwtUtil;

    /**存入一个生存时间为1天的长链接*/
    public void addLongUrlCache(String userId,String longUrl){
        long expiration = 60 * 60 * 24;
        redisTemplate.opsForValue().set("longUrl-"+longUrl,userId,expiration,TimeUnit.SECONDS);
    }

    /**删除一个长链接*/
    public void  deleteLongUrlCache(String longUrl){
        redisTemplate.delete("longUrl-"+longUrl);
    }

    /**根据长链接获取账号*/
    public String getLongUrlCache(String longUrl){
        return redisTemplate.opsForValue().get("longUrl-"+longUrl);
    }

    /**存入一个生存时间为30天的jwt*/
    public void addJitRedisCacheOnMouth(String jwt){
        long expiration = 60 * 60 * 24 * 30;
        addJwtRedisCache(jwt,expiration);
    }

    /**
     * 添加jwt缓存,key 为 jwt-userId
     * @param jwt JWT
     */
    public void addJwtRedisCache(String jwt, long expiration){
        //获取jwt中的用户信息
        String userId = jwtUtil.verifyJWT(jwt).getUserId();
        //将jwt存入redis中
        redisTemplate.opsForValue().set("jwt-"+userId,jwt,expiration, TimeUnit.SECONDS);
    }

    /**
     * 添加验证码缓存
     * @param contact 联系方式
     * @param code 验证码
     * @param expiration 过期时间
     */
    public void addAuthCodeCache(String contact, String code, long expiration){
        redisTemplate.opsForValue().set("authCode-"+contact,code,expiration, TimeUnit.SECONDS);
    }

    /**
     * 获取验证码缓存
     * @param contact 联系方式
     * @return 验证码
     */
    public String getAuthCodeCache(String contact){
        return redisTemplate.opsForValue().get("authCode-"+contact);
    }

    /**
     * 删除验证码缓存
     * @param contact 联系方式
     */
    public void deleteAuthCodeCache(String contact) {
        redisTemplate.delete("authCode-" + contact);
    }

    /**
     * 根据jwt，删除jwt缓存
     * @param jwt JWT
     */
    public void deleteJwtRedisCache(String jwt){
        //获取jwt中的用户信息
        String userId = jwtUtil.verifyJWT(jwt).getUserId();
        //删除redis中的jwt
        redisTemplate.delete("jwt-"+userId);
    }

    /**
     * 根据userId，删除jwt缓存
     * @param userId 用户id
     */
    public void deleteJwtRedisCacheByUserId(String userId){
        redisTemplate.delete("jwt-"+userId);
    }

    /**
     * 判断jwt是否存在
     * @param userInfo 用户信息
     * @return boolean
     */
    public boolean isJwtExist(JwtUserInfo userInfo) {
        return redisTemplate.opsForValue().get("jwt-"+userInfo.getUserId()) != null;
    }

    /**
     * 获取jwt
     */
    public String getJwt(String userId){
        return redisTemplate.opsForValue().get("jwt-"+userId);
    }

    /**
     * 获取jwt
     */
    public String getJwt(JwtUserInfo userInfo){
        return redisTemplate.opsForValue().get("jwt-"+userInfo.getUserId());
    }
}