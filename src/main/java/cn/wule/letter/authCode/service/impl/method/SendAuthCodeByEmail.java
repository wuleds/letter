package cn.wule.letter.authCode.service.impl.method;
//汉江师范学院 数计学院 吴乐创建于2023/12/12 02:10

import cn.wule.letter.authCode.service.SendAuthCodeService;
import cn.wule.letter.util.EmailUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SendAuthCodeByEmail implements SendAuthCodeService {
    @Resource
    private EmailUtil emailUtil;
    @Override
    public void sendAuthCode(String contact, String code) {
        log.info("发送验证码到邮箱：{}", contact);
        //发送验证码
        emailUtil.sendAuthEmail(contact, code);
    }
}