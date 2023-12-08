package cn.wule.letter.util;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 01:47

import cn.wule.letter.model.JwtUserInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisUtil
{
    @Resource
    StringRedisTemplate redisTemplate;
    @Resource
    JwtUtil jwtUtil;

    /**
     * 添加jwt缓存
     * @param jwt JWT
     */
    public void addJwtRedisCache(String jwt){
        //获取jwt中的用户信息
        String userId = jwtUtil.verifyJWT(jwt).getUseId();
        //将jwt存入redis中
        redisTemplate.opsForValue().set(userId,jwt);
    }

    /**
     * 删除jwt缓存
     * @param jwt JWT
     */
    public void deleteJwtRedisCache(String jwt){
        //获取jwt中的用户信息
        String userId = jwtUtil.verifyJWT(jwt).getUseId();
        //将jwt存入redis中
        redisTemplate.delete(userId);
    }

    /**
     * 判断jwt是否存在
     * @param userInfo 用户信息
     * @return boolean
     */
    public boolean isJwtExist(JwtUserInfo userInfo) {
        return redisTemplate.opsForValue().get(userInfo.getUseId()) != null;
    }

    public String getJwt(JwtUserInfo userInfo){
        return redisTemplate.opsForValue().get(userInfo.getUseId());
    }
}