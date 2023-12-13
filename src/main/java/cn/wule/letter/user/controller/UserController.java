package cn.wule.letter.user.controller;
//汉江师范学院 数计学院 吴乐创建于2023/12/10 0:47

import cn.wule.letter.authCode.service.AuthCodeService;
import cn.wule.letter.log.service.LoginLogService;
import cn.wule.letter.log.service.SigninLogService;
import cn.wule.letter.model.JwtUserInfo;
import cn.wule.letter.model.log.LoginLog;
import cn.wule.letter.model.log.SigninLog;
import cn.wule.letter.model.user.User;
import cn.wule.letter.user.service.UserInfoService;
import cn.wule.letter.user.vo.Contact;
import cn.wule.letter.user.vo.ContactIm;
import cn.wule.letter.user.vo.SigninVo;
import cn.wule.letter.user.service.UserService;
import cn.wule.letter.util.JsonUtil;
import cn.wule.letter.util.JwtUtil;
import cn.wule.letter.util.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                //添加redis记录
                redisUtil.addJitRedisCacheOnMouth(jwt);
                //可能存在老记录，要删除
                redisUtil.deleteJwtRedisCache(jwt);
                //记录日志
                loginLogService.insertLog(loginLog);
            }
        }
        return jsonUtil.createResponseModelJsonByString(code, msg, data);
    }

    @PostMapping("/signin")
    public String signin(@RequestBody SigninVo signinVo, HttpServletRequest request) {
        String code = "500";
        String msg = "服务器内部错误";
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
}