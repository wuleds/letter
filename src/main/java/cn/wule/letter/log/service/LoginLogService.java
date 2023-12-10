package cn.wule.letter.log.service;

import cn.wule.letter.model.log.LoginLog;

public interface LoginLogService {
    void insertLog(String userId, String userName, String ip, String host,String code,String msg);
}