package cn.wule.letter.log.service;

import cn.wule.letter.model.log.LoginLog;

public interface LoginLogService {
    void insertLog(LoginLog loginLog);
}