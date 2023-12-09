package cn.wule.letter.user.controller;
//汉江师范学院 数计学院 吴乐创建于2023/12/10 0:47

import cn.wule.letter.model.user.User;
import cn.wule.letter.user.service.UserService;
import cn.wule.letter.util.JsonUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController
{
    @Resource
    private UserService userService;
    @Resource
    private JsonUtil jsonUtil;

    @PostMapping("/signin")
    public String signin(@RequestBody User user) {
        String result = jsonUtil.createResponseModelJsonByString("500", "服务器内部错误", null);
        if (user == null) {
            result = jsonUtil.createResponseModelJsonByString("400", "User对象为空", null);
        }else if (user.getUserName() == null || user.getPassword() == null) {
            result = jsonUtil.createResponseModelJsonByString("400", "User对象参数为空", null);
        }else if(user.getUserName().length() < 2 || user.getUserName().length() > 16
                || user.getPassword().length() < 6 || user.getPassword().length() > 128) {
            result = jsonUtil.createResponseModelJsonByString("400", "用户名或密码的长度不符合要求", null);
        }else {
            String userId = userService.addUser(user.getUserName(), user.getPassword());
            if (userId != null) {
                result = jsonUtil.createResponseModelJsonByString("200", "注册成功", userId);
                //TODO:注册成功后服务器进行的必要操作,例如日志记录等

            }
        }
        return result;
    }
}