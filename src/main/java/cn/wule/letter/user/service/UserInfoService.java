package cn.wule.letter.user.service;

import cn.wule.letter.model.user.UserInfo;

public interface UserInfoService {
    void addUserInfo(String userId);
    UserInfo getUserInfo(String userId);

    void updateUserInfo(String userId,String userName,String userSex,String userPhoto,String userTalk);
    void deleteUserInfo(String userId);

    public String getUserInfoPhoto(String userId);
}