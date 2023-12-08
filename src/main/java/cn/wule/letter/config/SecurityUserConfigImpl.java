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

    public UserDetailsService userDetailsService(){
        UserDetails user1 = User.builder()
                .username("u1")
                .roles("user")
                .password(passwordEncoder().encode("123456"))
                .build();
        UserDetails user2 = User.builder()
                .username("t2")
                .authorities("teacher:add")
                .password(passwordEncoder().encode("123456"))
                .build();
        UserDetails user3 = User.builder()
                .username("t3")
                .authorities("teacher:query","teacher:delete","teacher:update")
                .password(passwordEncoder().encode("123456"))
                .build();
        UserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(user1);
        manager.createUser(user2);
        manager.createUser(user3);
        return manager;
    }

    /**
     * 配置密码加密器，自定义必须配置
     */

    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}