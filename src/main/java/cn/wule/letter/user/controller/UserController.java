package cn.wule.letter.user.controller;
//汉江师范学院 数计学院 吴乐创建于2023/12/10 0:47

import cn.wule.letter.log.service.LoginLogService;
import cn.wule.letter.model.JwtUserInfo;
import cn.wule.letter.model.user.User;
import cn.wule.letter.user.service.UserService;
import cn.wule.letter.util.JsonUtil;
import cn.wule.letter.util.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
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
    private LoginLogService loginLogService;

    @PostMapping("/login")
    public String login(User user, HttpServletRequest request){
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
        }else if(user.getUserId().length() < 6 || user.getUserId().length() > 16
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
                //返回JWT
                code = "200";
                msg = "登录成功";
                data = jwt;
                //记录日志
                loginLogService.insertLog(userId,userName,request.getRemoteAddr(),request.getRemoteHost(),"200","登录成功");
            }
        }
        return jsonUtil.createResponseModelJsonByString(code, msg, data);
    }

    @PostMapping("/signin")
    public String signin(@RequestBody User user) {
        String result = jsonUtil.createResponseModelJsonByString("500", "服务器内部错误", null);
        if (user == null) {
            result = jsonUtil.createResponseModelJsonByString("400", "User对象为空", null);
        }else if (user.getUserName() == null || user.getUserPassword() == null) {
            result = jsonUtil.createResponseModelJsonByString("400", "User对象参数为空", null);
        }else if(user.getUserName().length() < 2 || user.getUserName().length() > 16
                || user.getUserPassword().length() < 6 || user.getUserPassword().length() > 128) {
            result = jsonUtil.createResponseModelJsonByString("400", "用户名或密码的长度不符合要求", null);
        }else {
            String userId = userService.addUser(user.getUserName(), user.getUserPassword());
            if (userId != null) {
                result = jsonUtil.createResponseModelJsonByString("200", "注册成功", userId);
                //TODO:注册成功后服务器进行的必要操作,例如日志记录等

            }
        }
        return result;
    }
}