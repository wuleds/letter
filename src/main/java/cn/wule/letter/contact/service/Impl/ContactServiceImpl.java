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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


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

    /** 向用户的联系人列表添加联系人*/
    @Override
    public boolean updateContactList(String userId, String contactId) {
        //获取用户联系人列表
        //反序列为Set<String>格式,添加信息
        //序列化为json,存入数据库
        Contact contact = contactDao.getUserContactById(userId);
        String setJson = contact.getContactList();
        //获取用户联系人列表的Set。
        HashSet<String> contactSet;
        if(!Objects.equals(setJson, "") && setJson != null){
            try {
                contactSet = om.readValue(setJson, new TypeReference<>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }else {
            contactSet = new HashSet<>();
        }
        contactSet.add(contactId);
        String contactSetJson;
        try {
            contactSetJson = om.writeValueAsString(contactSet);
        } catch (JsonProcessingException e) {
            log.error("序列化用户联系人列表失败");
            return false;
        }
        contactDao.setContact(userId,contactSetJson);
        return true;
    }

    /**删除联系人*/
    @Override
    public boolean deleteContact(String userId, String contactId) {
        //获取用户联系人列表
        //反序列为Set<String>格式,删除信息
        //序列化为json格式,存入数据库
        Contact contact = contactDao.getUserContactById(userId);
        String json = contact.getContactList();
        //获取用户联系人列表的Set。
        HashSet<String> contactSet;
        try {
            contactSet = om.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //删除联系人
        contactSet.remove(contactId);
        //获取对方的联系人列表
        Contact contact2 = contactDao.getUserContactById(contactId);
        //反序列为Set<String>格式,删除信息
        //序列化为json格式,存入数据库
        String json2 = contact2.getContactList();
        //获取用户联系人列表的Set。
        HashSet<String> contactSet2;
        try {
            contactSet2 = om.readValue(json2, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //删除联系人
        contactSet2.remove(userId);

        String contactListJson;
        try {
            contactListJson = om.writeValueAsString(contactSet);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //存入数据库
        String contactListJson2;
        try {
            contactListJson2 = om.writeValueAsString(contactSet2);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if( contactDao.setContact(userId,contactListJson) && contactDao.setContact(contactId,contactListJson2)){
            return true;
        }else {
            contactDao.setContact(userId,json);
            contactDao.setContact(contactId,json2);
            return false;
        }
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
        //更新联系人请求状态,由被请求者处理
        contactDao.handleAddContact(fromUserId,toUserId,status,NowDate.getNowDate());
        //如果同意,则添加联系人
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
        String json = contact.getContactList();
        //获取用户联系人列表的Set。

        if(json != null && !json.isEmpty() && !Objects.equals(json, "")) {
            Set<String> contactSet;
            try {
                contactSet = om.readValue(json, new TypeReference<>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            for (String contactId : contactSet) {
                ContactInfo contactInfo = new ContactInfo();
                contactInfo.setContactId(contactId);
                UserInfo userInfo = userInfoService.getUserInfo(contactId);
                contactInfo.setContactName(userInfo.getUserName());
                contactInfo.setContactPhoto(userInfo.getUserPhoto());
                list.add(contactInfo);
            }
        }
        return list;
    }

    /**搜索联系人*/
    @Override
    public ContactInfo searchContact(String contactId) {
        ContactInfo contactInfo = new ContactInfo();
        UserInfo userInfo = userInfoService.getUserInfo(contactId);
        if(userInfo == null){
            return null;
        }
        contactInfo.setContactId(contactId);
        contactInfo.setContactName(userInfo.getUserName());
        contactInfo.setContactPhoto(userInfo.getUserPhoto());

        return contactInfo;
    }

    /**获取联系人请求列表*/
    @Override
    public List<ContactRequest> getContactRequestList(String userId) {
        return contactDao.getAddContactList(userId);
    }

    /**拉黑*/
    public boolean blackList(String userId,String contactId){
        //获取用户黑名单
        String blackListJson = contactDao.getBlackList(userId);
        //反序列为Set<String>格式,添加信息
        //序列化为json格式,存入数据库
        HashSet<String> blackList;
        if(blackListJson != null && !blackListJson.isEmpty() && !Objects.equals(blackListJson, "")) {
            try {
                blackList = om.readValue(blackListJson, new TypeReference<>() {});
                blackList.add(contactId);
                blackListJson = om.writeValueAsString(blackList);
                contactDao.setBlackList(userId,blackListJson);

                return true;
            } catch (JsonProcessingException e) {
                log.info("反序列化黑名单失败");
            }
        }
        return false;
    }
}