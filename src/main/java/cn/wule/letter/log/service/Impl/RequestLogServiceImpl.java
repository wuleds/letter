package cn.wule.letter.log.service.Impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 2:48

import cn.wule.letter.log.dao.RequestLogDao;
import cn.wule.letter.log.service.RequestLogService;
import cn.wule.letter.model.log.RequestLog;
import cn.wule.letter.util.NowDate;
import cn.wule.letter.util.UUIDUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请求过滤日志服务实现类
 */
@Service
public class RequestLogServiceImpl implements RequestLogService {
    @Resource
    RequestLogDao requestLogDao;

    @Override
    public void insertLog(RequestLog requestLog) {
        requestLogDao.insertLog(UUIDUtil.getUUID(),requestLog.getIp(),requestLog.getHost(),
                requestLog.getUri(),requestLog.getCode(),requestLog.getMsg(),requestLog.getUserName(),
                requestLog.getUserId(),requestLog.getUserInfo() ,NowDate.getNowDate());
    }
}