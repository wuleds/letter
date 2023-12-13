package cn.wule.letter.log.service.Impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/12 01:16

import cn.wule.letter.log.dao.SigninLogDao;
import cn.wule.letter.log.service.SigninLogService;
import cn.wule.letter.model.log.SigninLog;
import cn.wule.letter.util.NowDate;
import cn.wule.letter.util.UUIDUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SigninLogServiceImpl implements SigninLogService {
    @Resource
    private SigninLogDao signinLogDao;

    @Override
    public void insertLog(SigninLog signinLog) {
        signinLogDao.insertLog(UUIDUtil.getUUID(), signinLog.getUserId(), signinLog.getUserName(),
                NowDate.getNowDate(), signinLog.getIp(), signinLog.getHost(),
                signinLog.getCode(), signinLog.getMsg());
    }
}