package cn.wule.letter.log.service;

import cn.wule.letter.model.log.RequestLog;
import org.apache.ibatis.annotations.Select;

public interface RequestLogService
{
    void insertLog(String ip, String host, String uri, String code, String msg, String userName, String userId, String userInfo);
}