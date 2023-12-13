package cn.wule.letter.authCode.service;

/**
 * 验证码服务接口
 */
public interface AuthCodeService {
    /**
     * 发送验证码
     * @param method 验证方法，可能是邮箱，或者手机号
     * @param contact 联系方式，可能是邮箱，或者手机号
     */
    void sendAuthCode(String method, String contact);

    /**
     * 验证验证码
     * @param method 验证方法，可能是邮箱，或者手机号
     * @param contact 联系方式，可能是邮箱，或者手机号
     * @param code 验证码
     */
    boolean checkAuthCode(String method, String contact, String code);
}