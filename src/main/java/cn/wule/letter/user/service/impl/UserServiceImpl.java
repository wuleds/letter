package cn.wule.letter.user.service.impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 23:09

import cn.wule.letter.log.service.UserSigninLogService;
import cn.wule.letter.model.user.User;
import cn.wule.letter.user.dao.UserDao;
import cn.wule.letter.user.dao.UserInfoDao;
import cn.wule.letter.user.service.UserService;
import cn.wule.letter.util.UserUtil;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private UserInfoDao userInfoDao;
    @Resource(name = "userSigninLogServiceImpl")
    private UserSigninLogService userSigninLogService;

    public User getUserById(String userId) {
        return userDao.getUserById(userId);
    }

    /**
     * 添加普通用户
     * @param userName 用户名
     * @param userPassword 用户密码
     * @return 是否添加成功,最多获取10次用户id。
     */
    @Override
    public String addUser(String userName, String userPassword) {
        //创建新的用户Id
        int i = 1;
        long newId;
        do {
            //如果创建失败则重新创建
             newId = UserUtil.createUserId();
             if(i++ > 10){
                 return null;
             }
        }while (newId == -1);
        //密码加密
        String password = new BCryptPasswordEncoder().encode(userPassword);
        String userId = String.valueOf(newId);
        //添加到用户表
        userDao.addNormalUser(userId,userName,password);
        //添加用户信息表
        userInfoDao.addUserInfo(userId);
        //注册的日志记录
        userSigninLogService.addUserSigninLog(userId);
        //TODO 用户的好友表
        //TODO 用户的群组表
        //TODO 用户的订阅频道表
        return userId;
    }
}