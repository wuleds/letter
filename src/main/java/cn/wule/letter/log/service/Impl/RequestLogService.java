package cn.wule.letter.log.service.Impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 2:48

import cn.wule.letter.log.dao.RequestLogDao;
import cn.wule.letter.log.service.LogService;
import cn.wule.letter.model.log.LogBase;
import cn.wule.letter.model.log.RequestLog;
import cn.wule.letter.util.UUIDUtil;
import jakarta.annotation.Resource;

/**
 * 请求过滤日志服务实现类
 */
public class RequestLogService implements LogService {
    @Resource
    RequestLogDao requestLogDao;
    @Override
    public void insertLog(LogBase logBase) {
        RequestLog requestLog = (RequestLog)logBase ;
        requestLogDao.insertLog(UUIDUtil.getUUID(),requestLog.getIp(),requestLog.getHost(),requestLog.getUri(),requestLog.getCode(),requestLog.getMsg(),requestLog.getUserName(),requestLog.getUserId(),requestLog.getUserInfo());
    }
}