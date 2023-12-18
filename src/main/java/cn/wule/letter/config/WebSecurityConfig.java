package cn.wule.letter.config;
//汉江师范学院 数计学院 吴乐创建于2023/11/10 23:01

import cn.wule.letter.filter.RequestJwtFilter;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig{
    /*@Resource
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Resource
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;*/
    @Resource
    private RequestJwtFilter requestJwtFilter;
    @Resource
    private AppAccessDenyHandler appAccessDenyHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //在用户名密码认证过滤器前添加过滤器
        http.addFilterBefore(requestJwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(
                (authorize) -> authorize
                /*.requestMatchers("/root/**").hasAuthority("root:all")*/
                        .requestMatchers("/signin").permitAll()
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/test/principal").hasAuthority("main:add")
                .anyRequest()
                .authenticated());
        http.formLogin(AbstractHttpConfigurer::disable);
        http.logout((AbstractHttpConfigurer::disable));
        http.exceptionHandling((exceptionHandling) -> exceptionHandling.accessDeniedHandler(appAccessDenyHandler));

        //不创建session
        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //禁止同源保护
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}