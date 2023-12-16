package cn.wule.letter.user.controller;
//汉江师范学院 数计学院 吴乐创建于2023/12/10 0:47

import cn.wule.letter.authCode.service.AuthCodeService;
import cn.wule.letter.log.service.LoginLogService;
import cn.wule.letter.log.service.SigninLogService;
import cn.wule.letter.model.JwtUserInfo;
import cn.wule.letter.model.log.LoginLog;
import cn.wule.letter.model.log.SigninLog;
import cn.wule.letter.model.user.User;
import cn.wule.letter.model.user.UserInfo;
import cn.wule.letter.user.service.UserInfoService;
import cn.wule.letter.user.vo.*;
import cn.wule.letter.user.service.UserService;
import cn.wule.letter.util.JsonUtil;
import cn.wule.letter.util.JwtUtil;
import cn.wule.letter.util.RandomString;
import cn.wule.letter.util.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController
{
    @Resource(name = "userServiceImpl")
    private UserService userService;
    @Resource
    private JsonUtil jsonUtil;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private LoginLogService loginLogService;
    @Resource
    private SigninLogService signinLogService;
    @Resource
    private AuthCodeService authCodeService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private RandomString randomString;

    @PostMapping("/login")
    public String login(@RequestBody User user, HttpServletRequest request){
        log.info("用户登录");
        String code = "500";
        String msg = "服务器内部错误";
        String data = null;
        if (user == null) {
            code = "400";
            msg = "User对象为空";
        }else if (user.getUserId() == null || user.getUserPassword() == null) {
            code = "400";
            msg = "User对象参数为空";
        }else if(user.getUserId().length() < 5 || user.getUserId().length() > 16
                || user.getUserPassword().length() < 6 || user.getUserPassword().length() > 128) {
            code = "400";
            msg = "账号或密码的长度不符合要求";
        }else {
            User userCheck = userService.checkUser(user.getUserId(), user.getUserPassword());
            if (userCheck != null) {
                //登录成功后服务器进行的必要操作
                //创建jwt
                String userName = userCheck.getUserName();
                String userId = userCheck.getUserId();
                JwtUserInfo jwtUserInfo = JwtUserInfo.builder().userId(userId).userName(userName).build();
                String jwt = jwtUtil.createJwtOnMouth(jwtUserInfo);
                code = "200";
                msg = "登录成功";
                data = jwt;
                LoginLog loginLog = LoginLog.builder()
                        .userId(userId)
                        .userName(userName)
                        .ip(request.getRemoteAddr())
                        .host(request.getRemoteHost())
                        .code("200")
                        .msg("登录成功").build();
                //可能存在老记录，要删除
                redisUtil.deleteJwtRedisCacheByUserId(userId);
                //添加redis记录
                redisUtil.addJitRedisCacheOnMouth(jwt);
                //记录日志
                loginLogService.insertLog(loginLog);
            }
        }
        return jsonUtil.createResponseModelJsonByString(code, msg, data);
    }

    @PostMapping("/signin")
    public String signin(@RequestBody SigninVo signinVo, HttpServletRequest request) {
        String code;
        String msg;
        String data = null;
        if (signinVo == null) {
            code = "400";
            msg = "对象为空";
        }else if (signinVo.getUserName() == null || signinVo.getPassword() == null
                || signinVo.getSecondPassword() == null || signinVo.getMethod() == null
                || signinVo.getCode() == null || signinVo.getContact() == null) {
            code = "400";
            msg = "参数为空";
        }else if(signinVo.getUserName().length() < 2 || signinVo.getUserName().length() > 16
                || signinVo.getPassword().length() < 6 || signinVo.getPassword().length() > 128
                || signinVo.getSecondPassword().length() < 6 || signinVo.getSecondPassword().length() > 128) {
            code = "400";
            msg = "用户名,密码或确认密码的长度不符合要求";
        }else if (!signinVo.getPassword().equals(signinVo.getSecondPassword())) {
            code = "400";
            msg = "两次输入的密码不一致";
        }else {
            //各种信息全部都有，接下来进行验证码的验证
            if (authCodeService.checkAuthCode(signinVo.getMethod(), signinVo.getContact(), signinVo.getCode())) {
                //验证码正确，销毁验证码，防止二次利用，然后进行注册
                Contact contact = new ContactIm(signinVo.getContact(),signinVo.getMethod());
                String userId = userService.addUser(signinVo.getUserName(), signinVo.getPassword(),contact);
                if (userId != null) {
                    code = "200";
                    msg = "注册成功";
                    //注册成功后返回用户id
                    data = userId;
                    //记录日志
                    SigninLog signinLog = SigninLog.builder()
                            .userId(userId)
                            .userName(signinVo.getUserName())
                            .ip(request.getRemoteAddr())
                            .host(request.getRemoteHost())
                            .code("200")
                            .msg("注册成功").build();
                    signinLogService.insertLog(signinLog);
                }else {
                    code = "500";
                    msg = "注册失败";
                }
            }else {
                code = "400";
                msg = "验证码错误";
            }
        }
        return jsonUtil.createResponseModelJsonByString(code, msg, data);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        String jwt = request.getHeader("Authorization");
        if(jwt == null || jwt.isEmpty()){
            return jsonUtil.createResponseModelJsonByString("400","jwt为空",null);
        }
        //删除redis中的jwt
        redisUtil.deleteJwtRedisCache(jwt);
        return jsonUtil.createResponseModelJsonByString("200","退出登录成功",null);
    }

    @PostMapping("/forget")
    public String forget(@RequestBody ForgetVo forgetVo,HttpServletRequest request){
        if(forgetVo == null){
            return jsonUtil.createResponseModelJsonByString("400","对象为空",null);
        }
        String method = forgetVo.getMethod();
        String userId = forgetVo.getUserId();
        String contact = forgetVo.getContact();
        String authCode = forgetVo.getAuthCode();
        if(method == null || userId == null || contact == null || authCode == null || authCode.isEmpty() || method.isEmpty() || userId.isEmpty() || contact.isEmpty()){
            return jsonUtil.createResponseModelJsonByString("400","参数为空",null);
        }
        //根据账号获取user_info表中的用户信息
        UserInfo userInfo = userInfoService.getUserInfo(userId);
        if (userInfo == null){
            return jsonUtil.createResponseModelJsonByString("400","账号不存在",null);
        }
        log.info(String.valueOf(userInfo));
        String userPhone = userInfo.getUserPhone();
        String userEmail = userInfo.getUserEmail();
        if(userEmail == null && userPhone == null){
            return jsonUtil.createResponseModelJsonByString("400","该账号未绑定联系方式",null);
        }
        //确定用户的联系方式与预先设置的一样。
        switch (method){
            case "phone":
                if(!Objects.equals(userPhone, contact)){
                    return jsonUtil.createResponseModelJsonByString("400","手机号错误",null);
                }
                break;
            case "email":
                if(!Objects.equals(userEmail, contact)){
                    return jsonUtil.createResponseModelJsonByString("400","邮箱错误",null);
                }
                break;
            default:
                return jsonUtil.createResponseModelJsonByString("400","参数错误",null);
        }
        //验证验证码
        if(!authCodeService.checkAuthCode(method,contact,authCode)){
            return jsonUtil.createResponseModelJsonByString("400","验证码错误",null);
        }

        //移除jwt
        redisUtil.deleteJwtRedisCacheByUserId(userId);
        //生成长链接,并存入redis
        String longUrl = randomString.getLongLink();
        redisUtil.addLongUrlCache(userId,longUrl);
        //向联系方式发送长链接
        longUrl = "http://" + request.getServerName() + "/user/reset/" + longUrl;
        authCodeService.sendLongUrl(method,contact,longUrl);
        return jsonUtil.createResponseModelJsonByString("200","发送重置密码链接成功",null);
    }

    @PostMapping("/reset")
    public String resetPassword(@RequestBody ResetVo resetVo){
        String longUrl = resetVo.getLongUrl();
        String password = resetVo.getPassword();
        String secondPassword = resetVo.getSecondPassword();
        //根据长链接获取用户id
        String userId = redisUtil.getLongUrlCache(longUrl);
        if(userId == null){
            return jsonUtil.createResponseModelJsonByString("400","链接已失效",null);
        }
        if(password == null || secondPassword == null || password.isEmpty() || secondPassword.isEmpty()){
            return jsonUtil.createResponseModelJsonByString("400","参数为空",null);
        }
        if(password.length() < 6 || password.length() > 128 || secondPassword.length() < 6 || secondPassword.length() > 128){
            return jsonUtil.createResponseModelJsonByString("400","密码长度不符合要求",null);
        }
        if(!Objects.equals(password,secondPassword)){
            return jsonUtil.createResponseModelJsonByString("400","两次输入的密码不一致",null);
        }
        //修改密码
        if(userService.updatePassword(userId,password)) {
            //移除jwt
            redisUtil.deleteJwtRedisCacheByUserId(userId);
            //移除长链接
            redisUtil.deleteLongUrlCache(longUrl);
            return jsonUtil.createResponseModelJsonByString("200", "修改密码成功", null);
        }else {
            return jsonUtil.createResponseModelJsonByString("500","修改密码失败",null);
        }

    }
}