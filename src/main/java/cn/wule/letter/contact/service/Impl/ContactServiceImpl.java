package cn.wule.letter.contact.service.Impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/21 18:42

import cn.wule.letter.contact.dao.ContactDao;
import cn.wule.letter.contact.model.Contact;
import cn.wule.letter.contact.model.ContactInfo;
import cn.wule.letter.contact.model.ContactRequest;
import cn.wule.letter.contact.model.ContactRequestHandle;
import cn.wule.letter.contact.service.ContactService;
import cn.wule.letter.model.user.UserInfo;
import cn.wule.letter.user.service.UserInfoService;
import cn.wule.letter.util.NowDate;
import cn.wule.letter.util.UUIDUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 联系人模块业务层
 */
@Service
@Slf4j
public class ContactServiceImpl implements ContactService
{
    @Resource
    private ContactDao contactDao;
    @Resource
    private ObjectMapper om;
    @Resource
    private UserInfoService userInfoService;

    /**为新注册的用户添加联系人表列*/
    public void addContactList(String userId,String userName) {
        contactDao.addContactList(userId,userName);
    }

    /**存储添加联系人的请求*/
    @Override
    public void addContactRequest(String fromUserId, String toUserId, String info) {
        //如果已经发送过请求且该请求未被处理，则不能再次发送。
        if(contactDao.getAddContact(fromUserId,toUserId) == null){
            contactDao.addAddContact(UUIDUtil.getUUID(),fromUserId,toUserId,info, NowDate.getNowDate());
        }else {
            //但允许修改已经发送的请求的备注。
            contactDao.updateAddContact(fromUserId,toUserId,info,NowDate.getNowDate());
        }
    }

    /** 更新用户的联系人列表*/
    @Override
    public boolean updateContactList(String userId, String contactId) {
        //获取用户好友列表
        //反序列为Set<String>格式,添加信息
        //序列化为json格式,存入数据库
        Contact contact = contactDao.getUserContactById(userId);
        String json = contact.getFriendList();
        //获取用户好友列表的Set。
        Set<String> friendList;
        if(json != null){
          friendList  = om.convertValue(json, om.getTypeFactory().constructCollectionType(Set.class, String.class));
        }else {
            friendList = new HashSet<>();
        }
        friendList.add(contactId);
        String friendListJson;
        try {
            friendListJson = om.writeValueAsString(friendList);
        } catch (JsonProcessingException e) {
            log.error("序列化用户好友列表失败");
            return false;
        }
        contactDao.setContact(userId,friendListJson);
        return true;
    }

    /**删除联系人*/
    @Override
    public boolean deleteContact(String userId, String contactId) {
        //获取用户好友列表
        //反序列为Set<String>格式,删除信息
        //序列化为json格式,存入数据库
        Contact contact = contactDao.getUserContactById(userId);
        String json = contact.getFriendList();
        //获取用户好友列表的Set。
        Set<String> friendList = om.convertValue(json, om.getTypeFactory().constructCollectionType(Set.class, String.class));
        //删除好友
        friendList.remove(contactId);

        String friendListJson;
        try {
            friendListJson = om.writeValueAsString(friendList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        contactDao.setContact(userId,friendListJson);
        return true;
    }

    /**处理联系人请求*/
    @Override
    public boolean handleAddContact(String userId,ContactRequestHandle handle) {
        ContactRequest contactRequest = contactDao.getAddContactById(handle.getRequestId(),userId);
        if(contactRequest == null){
            return false;
        }
        String fromUserId = contactRequest.getFromUserId();
        String toUserId = contactRequest.getToUserId();
        int status = handle.getStatus();
        //有处理结果，且该请求未被处理，且处理者就是被请求者。
        //更新好友请求状态,由被请求者处理
        contactDao.handleAddContact(fromUserId,toUserId,status,NowDate.getNowDate());
        //如果同意,则添加好友
        if (status == 1){
            return updateContactList(fromUserId, toUserId) && updateContactList(toUserId, fromUserId);
        }else {
            return true;
        }
    }

    /**获取联系人列表*/
    public List<ContactInfo> getContactList(String userId) {
        //先获取用户基本信息，再获取用户自定义信息。
        Contact contact = contactDao.getUserContactById(userId);
        List<ContactInfo> list = new ArrayList<>();
        //获取联系人列表的json格式
        String json = contact.getFriendList();
        //获取用户好友列表的Set。
        Set<String> contactList = om.convertValue(json, om.getTypeFactory().constructCollectionType(Set.class, String.class));

        for (String contactId : contactList) {
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setContactId(contactId);
            UserInfo userInfo = userInfoService.getUserInfo(contactId);
            contactInfo.setContactName(userInfo.getUserName());
            contactInfo.setContactPhoto(userInfo.getUserPhoto());
            list.add(contactInfo);
        }
        return list;
    }
}