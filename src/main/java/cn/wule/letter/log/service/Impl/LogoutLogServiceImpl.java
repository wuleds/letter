package cn.wule.letter.log.service.Impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 22:13

import cn.wule.letter.log.dao.LogoutLogDao;
import cn.wule.letter.log.service.LogoutLogService;
import cn.wule.letter.model.log.LogoutLog;
import cn.wule.letter.util.NowDate;
import cn.wule.letter.util.UUIDUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class LogoutLogServiceImpl implements LogoutLogService {
    @Resource
    LogoutLogDao logoutLogDao;

    @Override
    public void insertLog(String ip, String host, String code, String msg,String userName, String userId, String userInfo) {
        logoutLogDao.insertLog(UUIDUtil.getUUID(),ip,host,code,msg,userName,userId,userInfo,NowDate.getNowDate());
    }
}