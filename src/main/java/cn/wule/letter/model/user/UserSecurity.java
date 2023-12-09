package cn.wule.letter.model.user;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 22:38

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

public class UserSecurity implements UserDetails {
    @Getter
    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;
    public UserSecurity(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return Objects.equals(this.user.getDelFlag(), "1");
    }

    @Override
    public boolean isAccountNonLocked() {
        return Objects.equals(this.user.getDelFlag(), "1");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Objects.equals(this.user.getDelFlag(), "1");
    }

    @Override
    public boolean isEnabled() {
        return Objects.equals(this.user.getDelFlag(), "1");
    }
}