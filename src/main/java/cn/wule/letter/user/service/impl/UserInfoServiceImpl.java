package cn.wule.letter.user.service.impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/14 0:11

import cn.wule.letter.model.user.UserInfo;
import cn.wule.letter.user.dao.UserInfoDao;
import cn.wule.letter.user.service.UserInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 用户信息业务层*/
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoDao userInfoDao;
    @Override
    public void addUserInfo(String userId) {
        userInfoDao.addUserInfoById(userId);
    }

    @Override
    public UserInfo getUserInfo(String userId) {
        return userInfoDao.getUserInfoById(userId);
    }

    @Override
    public void updateUserInfo(String userId, String userSex, String userBirthday, String userAddress, String userPhone, String userEmail) {
        userInfoDao.updateUserInfo(userId, userSex, userBirthday, userAddress, userPhone, userEmail);
    }

    @Override
    public void deleteUserInfo(String userId) {
        userInfoDao.deleteUserInfoById(userId);
    }
}