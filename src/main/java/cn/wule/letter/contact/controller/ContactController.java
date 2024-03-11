package cn.wule.letter.contact.controller;
//汉江师范学院 数计学院 吴乐创建于2023/12/21 18:03

import cn.wule.letter.contact.model.Contact;
import cn.wule.letter.contact.model.ContactInfo;
import cn.wule.letter.contact.model.ContactRequest;
import cn.wule.letter.contact.model.ContactRequestHandle;
import cn.wule.letter.contact.service.ContactService;
import cn.wule.letter.model.user.User;
import cn.wule.letter.user.service.UserService;
import cn.wule.letter.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 联系人模块控制层
 * 用户关系处理，用户间可以互相添加为联系人，删除，拉黑名单等。
 * 在此过程中要确认用户身份，防止恶意操作，例如发送联系人请求，必须确认发送者的身份，直接从jwt中获取该用户的信息。
 */
@RestController
@Slf4j
@RequestMapping("/contact")
public class ContactController
{
    @Resource
    private ContactService contactService;
    @Resource
    private UserService userService;
    @Resource
    private JsonUtil jsonUtil;
    @Resource
    private ObjectMapper om;

    /**发送添加联系人请求*/
    @PostMapping("/add")
    public ResponseEntity<String> contactRequest(@RequestBody ContactRequest contactRequest)
    {
        String code;
        String msg;
        String fromUserId = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        if(contactRequest.getToUserId() == null || contactRequest.getToUserId().isEmpty()){
            code = "400";
            msg = "参数为空";
        }else if(contactRequest.getFromUserId().equals(contactRequest.getToUserId())) {
            code = "400";
            msg = "不能添加自己为好友";
        }else if(userService.getUserById(contactRequest.getToUserId()) == null || userService.getUserById(contactRequest.getFromUserId()) == null){
            code = "400";
            msg = "用户不存在";
        }else {
            contactService.addContactRequest(fromUserId, contactRequest.getToUserId(), contactRequest.getInfo());
            code = "200";
            msg = "发送好友请求成功";
        }
        return ResponseEntity.ok(jsonUtil.createResponseModelJsonByString(code,msg,null));
    }

    /**处理联系人请求*/
    @PostMapping("/handle")
    public String handleContactRequest(@RequestBody ContactRequestHandle handle)
    {
        String code;
        String msg;
        String toUserId = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        String requestId = handle.getRequestId();
        int status = handle.getStatus();
        if(requestId == null || requestId.isEmpty()) {
            code = "400";
            msg = "参数为空";
        }else if(status != 2 && status != 3) {
            code = "400";
            msg = "参数错误";
        } else {
            if(contactService.handleAddContact(toUserId,handle)){
                code = "200";
                msg = "处理好友请求成功";
            }else {
                code = "400";
                msg = "处理好友请求失败";
            }
        }
        return jsonUtil.createResponseModelJsonByString(code,msg,null);
    }

    /**删除联系人*/
    @PostMapping("/delete")
    public String deleteContact(@RequestBody String  deletedContactId)
    {
        String code;
        String msg;
        //获取当前用户信息。
        String fromUserId = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        if(deletedContactId == null || deletedContactId.isEmpty()) {
            code = "400";
            msg = "参数为空";
        }else if(fromUserId.equals(deletedContactId)) {
            code = "400";
            msg = "不能删除自己";
        }else if(userService.getUserById(deletedContactId) == null) {
            code = "400";
            msg = "用户不存在";
        }else {
            contactService.deleteContact(fromUserId, deletedContactId);
            code = "200";
            msg = "删除好友成功";
        }
        return jsonUtil.createResponseModelJsonByString(code,msg,null);
    }

    /**获取联系人列表*/
    @PostMapping("/get")
    public List<ContactInfo> getContactList(){
        String currentUserId = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        return contactService.getContactList(currentUserId);
    }

    /**根据账号获取联系人信息*/
    @PostMapping("/search")
    public String searchContact(@RequestBody Contact contact){
        String code;
        String msg;
        String  data = null;
        String userId = contact.getUserId();
        ContactInfo contactInfo;
        //对参数进行检查
        if(userId == null || userId.isEmpty()) {
            code = "400";
            msg = "参数为空";
        }else {
            contactInfo = contactService.searchContact(userId);
            if(contactInfo == null) {
                code = "400";
                msg = "用户不存在";
            }else {
                code = "200";
                msg = "查询成功";
                try {
                    data = om.writeValueAsString(contactInfo);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return jsonUtil.createResponseModelJsonByString(code,msg,data);
    }

}