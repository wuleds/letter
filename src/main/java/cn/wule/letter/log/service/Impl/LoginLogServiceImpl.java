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
    public void insertLog(String userId, String userName,String ip, String host,String code,String msg) {
        LoginLog loginLog = LoginLog.builder()
                .id(UUIDUtil.getUUID())
                .userId(userId)
                .userName(userName)
                .ip(ip)
                .host(host)
                .loginDate(NowDate.getNowDate())
                .code(code)
                .msg(msg)
                .build();
        loginLogDao.insertLog(loginLog);
    }
}