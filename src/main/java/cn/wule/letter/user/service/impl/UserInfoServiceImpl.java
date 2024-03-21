package cn.wule.letter.user.service.impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/14 0:11

import cn.wule.letter.model.user.UserInfo;
import cn.wule.letter.user.dao.UserDao;
import cn.wule.letter.user.dao.UserInfoDao;
import cn.wule.letter.user.service.UserInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 用户自定义信息业务层*/
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoDao userInfoDao;
    @Resource
    private UserDao userDao;
    @Override
    public void addUserInfo(String userId) {
        userInfoDao.addUserInfoById(userId);
    }

    /**根据账号获取用户自定义信息*/
    @Override
    public UserInfo getUserInfo(String userId) {
        UserInfo userInfo = userInfoDao.getUserInfoById(userId);
        return userInfo;
    }

    /**信息更新*/
    @Override
    public void updateUserInfo(String userId, String userName,String userSex, String userBirthday, String userAddress, String userPhone, String userEmail) {
        userInfoDao.updateUserInfo(userId, userName,userSex, userBirthday, userAddress, userPhone, userEmail);
        //当初设计数据库的时候user表和user_info表没有设计好，都有user_name字段，所以这里只能这样写了，改不动了。
        userDao.updateUserName(userId,userName);
    }

    /**删除信息*/
    @Override
    public void deleteUserInfo(String userId) {
        userInfoDao.deleteUserInfoById(userId);
    }

    public String getUserInfoPhoto(String userId) {
        return userInfoDao.getUserInfoPhoto(userId);
    }
}