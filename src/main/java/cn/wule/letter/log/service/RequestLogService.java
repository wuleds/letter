package cn.wule.letter.log.service;

import cn.wule.letter.model.log.RequestLog;
import org.apache.ibatis.annotations.Select;

public interface RequestLogService
{
    void insertLog(RequestLog requestLog);
}