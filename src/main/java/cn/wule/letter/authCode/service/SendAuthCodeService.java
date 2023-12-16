package cn.wule.letter.authCode.service;

/**
 * 验证码发送方式服务接口
 */
public interface SendAuthCodeService
{
    void sendAuthCode(String contact, String code);

    void sendLongUrl(String contact, String url);
}