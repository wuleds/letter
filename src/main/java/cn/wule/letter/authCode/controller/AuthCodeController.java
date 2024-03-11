package cn.wule.letter.authCode.controller;
//汉江师范学院 数计学院 吴乐创建于2023/12/12 23:39

import cn.wule.letter.authCode.service.AuthCodeService;
import cn.wule.letter.authCode.vo.AuthVo;
import cn.wule.letter.util.JsonUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthCodeController
{
    @Resource
    private AuthCodeService authCodeService;
    @Resource
    private JsonUtil jsonUtil;

    @PostMapping("/get")
    public String getAuthCode(@RequestBody AuthVo authVo)
    {
        String method = authVo.getMethod();
        String s = authVo.getS();
        log.info("method: " + method + " s: " + s);
        if(method == null || s == null || method.isEmpty() || s.isEmpty())
            return jsonUtil.createResponseModelJsonByString("400", "参数为空", null);
        if(!method.equals("email") && !method.equals("phone"))
            return jsonUtil.createResponseModelJsonByString("400", "联系方式不支持", null);
        //验证联系方式是否为邮箱
        if(method.equals("email") && !s.matches("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z]+)+$"))
            return jsonUtil.createResponseModelJsonByString("400", "邮箱格式错误", null);
        //验证联系方式是否为手机号
        if(method.equals("phone") && !s.matches("^1[0-9]{10}$"))
            return jsonUtil.createResponseModelJsonByString("400", "手机号格式错误", null);
        authCodeService.sendAuthCode(method, s);
        String result = jsonUtil.createResponseModelJsonByString("200", "验证码已发送", null);
        log.info(result);
        return result;
    }
}