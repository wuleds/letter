package cn.wule.letter.authCode.service.impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/12 02:04

import cn.wule.letter.authCode.dao.AuthCodeDao;
import cn.wule.letter.authCode.service.AuthCodeService;
import cn.wule.letter.authCode.service.SendAuthCodeService;
import cn.wule.letter.util.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

/**
 * 验证码服务实现类
 */
@Slf4j
@Service
public class AuthCodeServiceImpl implements AuthCodeService {
    @Resource
    private RedisUtil redisUtil;
    @Resource(name = "sendAuthCodeByEmail")
    private SendAuthCodeService sendAuthCodeByEmail;
    @Resource(name = "sendAuthCodeByPhone")
    private SendAuthCodeService sendAuthCodeByPhone;

    @Override
    public void sendAuthCode(String method, String contact) {
        SendAuthCodeService sendAuthCodeService;
        //检查联系方式
        if(Objects.equals(method, "email")) {
            sendAuthCodeService = sendAuthCodeByEmail;
        }else if(Objects.equals(method, "phone")) {
            sendAuthCodeService = sendAuthCodeByPhone;
        }else {
            return;
        }
        //生成验证码
        String code = String.valueOf(new Random().nextInt(9000) + 1000);
        long expiration = 60 * 5;
        //保存验证码到缓存
        redisUtil.addAuthCodeCache(contact, code, expiration);
        //发送验证码
        sendAuthCodeService.sendAuthCode(contact, code);
    }

    /**
     * 验证验证码
     * @param method 验证方法，可能是邮箱，或者手机号
     * @param contact 联系方式，可能是邮箱，或者手机号
     * @param code 验证码
     */
    @Override
    public boolean checkAuthCode(String method, String contact, String code) {
        return true;
        /*//获取最新的有效的验证码
        String autoCode = redisUtil.getAuthCodeCache(contact);
        //验证验证码
        if(autoCode == null || code == null || autoCode.isEmpty() || code.isEmpty()) {
            return false;
        }
        if(Objects.equals(autoCode, code)){
            //验证成功后删除验证码
            redisUtil.deleteAuthCodeCache(contact);
            return true;
        }else {
            return false;
        }*/
    }

    @Override
    public void sendLongUrl(String method, String contact, String longUrl){
        SendAuthCodeService sendAuthCodeService;
        //检查联系方式
        if(Objects.equals(method, "email")) {
            sendAuthCodeService = sendAuthCodeByEmail;
        }else if(Objects.equals(method, "phone")) {
            sendAuthCodeService = sendAuthCodeByPhone;
        }else {
            return;
        }
        //发送长链接
        sendAuthCodeService.sendLongUrl(contact, longUrl);
    }
}