package cn.wule.letter.authCode.service;

/**
 * 验证码服务接口
 */
public interface AuthCodeService {
    /**
     * 发送验证码
     * @param method 验证方法，可能是邮箱，或者手机号
     * @param s 邮件地址或电话号码
     */
    void sendAuthCode(String method, String s);

    /**
     * 验证验证码
     * @param method 验证方法，可能是邮箱，或者手机号
     * @param s 邮件地址或电话号码
     * @param code 验证码
     */
    boolean checkAuthCode(String method, String s, String code);

    /**
     *
     * @param method 验证方法，可能是邮箱，或者手机号
     * @param s 邮件地址或电话号码
     * @param longUrl 长链接
     */
    void sendLongUrl(String method, String s,String longUrl);
}