package cn.wule.letter.user.service;

import cn.wule.letter.model.user.UserInfo;

public interface UserInfoService {
    void addUserInfo(String userId);
    UserInfo getUserInfo(String userId);
    void updateUserInfo(String userId,String userSex,String userBirthday,String userAddress,String userPhone,String userEmail);
    void deleteUserInfo(String userId);
}