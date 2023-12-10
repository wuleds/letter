package cn.wule.letter.util;
//汉江师范学院 数计学院 吴乐创建于2023/12/11 0:15

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 密码加密工具类
 */
@Component
public class BCryptPwdUtil
{
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();
    public static String encode(String password){
        return encoder.encode(password);
    }

    /**
     * 比较密码和加密后的密码是否匹配
     * @param password 原始密码
     * @param encodePassword 加密密码
     * @return 匹配为true
     */
    public static boolean matches(String password, String encodePassword){
        return encoder.matches(password, encodePassword);
    }
}