package cn.wule.letter.user.service;

import cn.wule.letter.model.user.User;
import cn.wule.letter.user.vo.Contact;

public interface UserService {
    User getUserById(String userId);

    boolean updatePassword(String userId,String password);

    String addUser(String userName, String password, Contact contact);

    User checkUser(String userId,String password);
}