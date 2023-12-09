package cn.wule.letter.log.service.Impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/10 2:05

import cn.wule.letter.log.dao.UserSigninLogDao;
import cn.wule.letter.log.service.UserSigninLogService;
import cn.wule.letter.util.UUIDUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserSigninLogServiceImpl implements UserSigninLogService
{
    @Resource
    private UserSigninLogDao userSigninLogDao;

    /**
     * 添加用户注册的日志
     * @param userId 用户id
     */
    @Override
    public void addUserSigninLog(String userId) {
        String id = UUIDUtil.getUUID();
        userSigninLogDao.addUserSigninLog(id,userId);
    }
}