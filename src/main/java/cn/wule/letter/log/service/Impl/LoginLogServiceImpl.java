package cn.wule.letter.log.service.Impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/11 0:57

import cn.wule.letter.log.dao.LoginLogDao;
import cn.wule.letter.log.service.LoginLogService;
import cn.wule.letter.model.log.LoginLog;
import cn.wule.letter.util.NowDate;
import cn.wule.letter.util.UUIDUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LoginLogServiceImpl implements LoginLogService {
    @Resource
    private LoginLogDao loginLogDao;
    @Override
    public void insertLog(LoginLog loginLog) {
        loginLog.setId(UUIDUtil.getUUID());
        loginLog.setLoginDate(NowDate.getNowDate());
        loginLogDao.insertLog(UUIDUtil.getUUID(),loginLog.getUserId(),loginLog.getUserName(),
                loginLog.getLoginDate(),loginLog.getIp(),loginLog.getHost(),loginLog.getCode(),
                loginLog.getMsg());
    }
}