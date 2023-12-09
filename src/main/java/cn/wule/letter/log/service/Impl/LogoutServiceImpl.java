package cn.wule.letter.log.service.Impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 22:13

import cn.wule.letter.log.dao.LogoutLogDao;
import cn.wule.letter.log.service.LogoutService;
import cn.wule.letter.log.service.RequestLogService;
import cn.wule.letter.model.log.LogBase;
import cn.wule.letter.model.log.LogoutLog;
import cn.wule.letter.util.UUIDUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LogoutServiceImpl implements LogoutService {
    @Resource
    LogoutLogDao logoutLogDao;

    @Override
    public void insertLog(LogoutLog logoutLog) {
        logoutLog.setId(UUIDUtil.getUUID());
        logoutLogDao.insertLog(logoutLog);
    }
}