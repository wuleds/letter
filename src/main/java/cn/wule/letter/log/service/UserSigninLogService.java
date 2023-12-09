package cn.wule.letter.log.service;

public interface UserSigninLogService
{
    /**
     * 添加用户注册日志
     * @param userId 用户id
     */
    void addUserSigninLog(String userId);
}