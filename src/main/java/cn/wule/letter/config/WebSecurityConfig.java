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

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{
    /*@Resource
    private AppAuthenticationSuccessHandler appAuthenticationSuccessHandler;
    @Resource
    private AppAuthenticationFailHandler appAuthenticationFailHandler;
    @Resource
    private AppLogoutSuccessHandler appLogoutSuccessHandler;*/
    @Resource
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Resource
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;
    @Resource
    private RequestJwtFilter requestJwtFilter;
    @Resource
    private AppAccessDenyHandler appAccessDenyHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //在用户名密码认证过滤器前添加过滤器
        http.addFilterBefore(requestJwtFilter, UsernamePasswordAuthenticationFilter.class);
        //不创建session
        http.sessionManagement((sessionManagement) -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(
                (authorize) -> authorize
                /*.requestMatchers("/root/**").hasAuthority("root:all")*/
                        .requestMatchers("/signin").permitAll()
                        .requestMatchers("/user/login").permitAll()
                .anyRequest()
                .permitAll());
        http.formLogin(AbstractHttpConfigurer::disable);
                        //.loginPage("/ll").permitAll() //配置登录接口
//                        .usernameParameter("userId") //配置登录用户名参数
//                        .passwordParameter("password") //配置登录密码参数
//                        .loginProcessingUrl("/api/user/login").permitAll() //单击登录的接口
                        //.failureForwardUrl("/toLogin").permitAll() //登录失败的接口
                        //.successForwardUrl("/main") //登录成功的接口
                        //.successHandler(customAuthenticationSuccessHandler) //登录成功的处理器

        http.logout((logout)->
                logout
                        .logoutUrl("/logout") //配置登出接口
                        //.logoutSuccessUrl("/toLogin") //登出成功的接口
                        .logoutSuccessHandler(customLogoutSuccessHandler) //登出成功的处理器
                        .permitAll());
        http.exceptionHandling((exceptionHandling) -> exceptionHandling.accessDeniedHandler(appAccessDenyHandler));

        http.csrf(AbstractHttpConfigurer::disable);
        //禁止同源保护
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}