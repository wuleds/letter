package cn.wule.letter.user.service.impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 23:06

import cn.wule.letter.model.user.User;
import cn.wule.letter.model.user.UserSecurity;
import cn.wule.letter.user.service.UserPermissionService;
import cn.wule.letter.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 用于Spring Security获取到用户信息
 */
@Service
public class UserSecurityDetailServiceImpl implements UserDetailsService {
    @Resource
    private UserService userService;
    @Resource
    private UserPermissionService permissionService;
    /**
     * 通过用户id获取用户信息
     * @param userId 用户id
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userService.getUserById(userId);
        if(user == null || Objects.equals(user.getDelFlag(),"0")){
            throw new UsernameNotFoundException("用户不存在");
        }
        List<SimpleGrantedAuthority> authorities = permissionService.getUserPermissionByUserId(userId).stream().map(SimpleGrantedAuthority::new).toList();
        return new UserSecurity(user,authorities);
    }
}