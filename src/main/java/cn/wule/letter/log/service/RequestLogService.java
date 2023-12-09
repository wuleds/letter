package cn.wule.letter.log.service;

import cn.wule.letter.model.log.LogBase;
import cn.wule.letter.model.log.RequestLog;

public interface RequestLogService
{
    void insertLog(RequestLog requestLog);
}