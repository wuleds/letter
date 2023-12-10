package cn.wule.letter.user.service.impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 23:09

import cn.wule.letter.log.service.UserSigninLogService;
import cn.wule.letter.model.user.User;
import cn.wule.letter.user.dao.UserDao;
import cn.wule.letter.user.dao.UserInfoDao;
import cn.wule.letter.user.service.UserService;
import cn.wule.letter.util.BCryptPwdUtil;
import cn.wule.letter.util.UserUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
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
        String password = BCryptPwdUtil.encode(userPassword);
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

    /**
     * 查询用户加密后的密码，然后与输入的密码进行比较
     * @return boolean true则账号和密码正确，false则账号或密码错误
     */
    @Override
    public User checkUser(String userId,String password) {
        log.info("用户登录，用户id：{}",userId);
        String encodePassword = userDao.checkUser(userId);
        //将密码与加密后的密码进行比较
        if(BCryptPwdUtil.matches(password,encodePassword)){
            //获取用户信息
            return userDao.getUserById(userId);
        }else {
            return null;
        }
    }
}