package cn.wule.letter.user.service.impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 23:09

import cn.wule.letter.contact.service.ContactService;
import cn.wule.letter.model.user.User;
import cn.wule.letter.user.dao.UserDao;
import cn.wule.letter.user.dao.UserInfoDao;
import cn.wule.letter.user.service.UserService;
import cn.wule.letter.user.vo.ContactWay;
import cn.wule.letter.util.BCryptPwdUtil;
import cn.wule.letter.util.NowDate;
import cn.wule.letter.util.UserUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private UserInfoDao userInfoDao;
    @Resource
    private ContactService contactService;

    public User getUserById(String userId) {
        return userDao.getUserById(userId);
    }

    /**
     * 修改用户密码
     * 密码会加密 BCryptPwdUtil
     */
    @Override
    public boolean updatePassword(String userId, String password) {
        //密码加密
        String encodePassword = BCryptPwdUtil.encode(slatingPassword(password));
        return userDao.updateUserPassword(userId,encodePassword);
    }

    /**
     * 添加普通用户
     * @return 是否添加成功,最多获取10次用户id。
     */
    @Override
    @Transactional
    public String addUser(String userName, String userPassword, ContactWay contactWay) {
        //创建新的用户Id
        int i = 1;
        long newId;
        do {
            //如果创建失败则重新创建
             newId = UserUtil.createUserId();
             if(i++ > 10){
                 //创建失败，终止流程
                 return null;
             }
        }while (newId == -1);
        //密码加密
        String encodePassword = BCryptPwdUtil.encode(slatingPassword(userPassword));
        String userId = String.valueOf(newId);
        //添加用户表
        userDao.addNormalUser(userId,userName,encodePassword, NowDate.getNowDate());
        //添加用户信息
        userInfoDao.addUserInfoById(userId);
        userInfoDao.updateUserInfo(userId,userName,null,null,null);
        //添加用户的联系人列表
        contactService.addContactList(userId);
        //用户的群组列表
        userInfoDao.addUserGroupList(userId);
        //用户的订阅频道列表
        userInfoDao.addUserChannelList(userId);
        //用户的聊天列表
        userInfoDao.addUserChatList(userId);
        //添加用户黑名单列表
        userInfoDao.addBlackList(userId);
        return userId;
    }

    /**
     * 查询用户加密后的密码，然后与输入的密码进行比较
     * @return boolean true则账号和密码正确，false则账号或密码错误
     */
    @Override
    public User checkUser(String userId,String password) {
        String encodePassword = userDao.checkUser(userId);
        //将密码与加密后的密码进行比较
        if(BCryptPwdUtil.matches(slatingPassword(password),encodePassword)){
            //获取用户信息
            return userDao.getUserById(userId);
        }else {
            return null;
        }
    }

    @Override
    public String slatingPassword(String password) {
        String slat = "WUHVQjGFJyQRaV.3ZGuQrKUoPeFHV4@MwBFw!dExHyawozkQmX_7ktLKkzi4p7CDAw3DEWJfg.*u4X@3@rfGWjwv.aQ!pkB!ZmKj";
        return password + slat;
    }
}