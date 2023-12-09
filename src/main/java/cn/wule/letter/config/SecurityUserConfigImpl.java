package cn.wule.letter.config;
//汉江师范学院 数计学院 吴乐创建于2023/11/10 21:35

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * @author wule
 */
public class SecurityUserConfigImpl{
    /**
     * 配置密码加密器，自定义必须配置
     */
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}