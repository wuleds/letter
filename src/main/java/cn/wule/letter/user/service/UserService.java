package cn.wule.letter.user.service;

import cn.wule.letter.model.user.User;

public interface UserService {
    User getUserById(String userId);

    String addUser(String userName,String password);

    User checkUser(String userId,String password);
}