package cn.wule.letter.log.service;

import cn.wule.letter.model.log.LogoutLog;

public interface LogoutLogService
{
    void insertLog(String ip, String host, String code, String msg,String userName, String userId, String userInfo);
}