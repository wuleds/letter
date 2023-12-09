package cn.wule.letter.log.service;

import cn.wule.letter.model.log.LogoutLog;
import cn.wule.letter.model.log.RequestLog;

public interface LogoutService
{
    void insertLog(LogoutLog logoutLog);
}