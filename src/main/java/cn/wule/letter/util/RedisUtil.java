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
        long expiration = 1000L * 60 * 60 * 24;
        redisTemplate.opsForValue().set("longUrl-"+userId,longUrl,expiration,TimeUnit.MILLISECONDS);
    }

    /**存入一个生存时间为30天的jwt*/
    public void addJitRedisCacheOnMouth(String jwt){
        long expiration = 1000L * 60 * 60 * 24 * 30;
        addJwtRedisCache(jwt,expiration);
    }

    /**
     * 添加jwt缓存,key 为 userId
     * @param jwt JWT
     */
    public void addJwtRedisCache(String jwt, long expiration){
        //获取jwt中的用户信息
        String userId = jwtUtil.verifyJWT(jwt).getUserId();
        //将jwt存入redis中
        redisTemplate.opsForValue().set(userId,jwt,expiration, TimeUnit.MILLISECONDS);
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
        //将jwt存入redis中
        redisTemplate.delete(userId);
    }

    /**
     * 根据userId，删除jwt缓存
     * @param userId 用户id
     */
    public void deleteJwtRedisCacheByUserId(String userId){
        redisTemplate.delete(userId);
    }

    /**
     * 判断jwt是否存在
     * @param userInfo 用户信息
     * @return boolean
     */
    public boolean isJwtExist(JwtUserInfo userInfo) {
        return redisTemplate.opsForValue().get(userInfo.getUserId()) != null;
    }

    /**
     * 获取jwt
     */
    public String getJwt(String userId){
        return redisTemplate.opsForValue().get(userId);
    }

    /**
     * 获取jwt
     */
    public String getJwt(JwtUserInfo userInfo){
        return redisTemplate.opsForValue().get(userInfo.getUserId());
    }
}