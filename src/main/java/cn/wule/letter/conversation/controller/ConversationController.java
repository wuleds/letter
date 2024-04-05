package cn.wule.letter.conversation.controller;
//汉江师范学院 数计学院 吴乐创建于2024/3/13 0:14

import cn.wule.letter.conversation.model.Conversation;
import cn.wule.letter.conversation.service.PrivateConversationService;
import cn.wule.letter.model.user.User;
import cn.wule.letter.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 对话控制层，对话是聊天列表的单位，私聊，群组，频道都可以对话。
 */
@RestController
@RequestMapping("/conversation")
@Slf4j
public class ConversationController
{
    @Resource
    private PrivateConversationService privateConversationService;
    @Resource
    private JsonUtil jsonUtil;

    /**
     * 通过此请求新建对话
     * type 可能为private,group,channel。
     * 1.私聊：若已经新建过，则通过private_conversation表获取chatId，并获取历史记录，若新建，则生成chatId。
     * 2.群组：groupId即为chatId，每个群组都会新建一个以chatId命名的聊天信息表，获取chatId，并获取历史记录。
     * 3.频道：channelId即为chatId，每个频道都会新建一个以chatId命名的频道发布信息表，获取chatId，并获取历史发布记录。
     * toId 可能为userId,groupId,channelId, myId为当前用户的id。
     * data:{
     *     type:'',
     *     chatId:'',
     *     oneId:'',
     *     twoId:''
     * }
     * */
    @PostMapping("/new")
    public String  newConversation(@RequestBody Conversation conversation) {
        String code = "400";
        String msg;
        String type = conversation.getType();
        String data = null;
        String myId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        String toId = conversation.getToId();

        if(type == null || toId == null || myId == null) {
            msg = "参数为空";
        }else if(!type.equals("private") && !type.equals("group") && !type.equals("channel")) {
            msg = "对话类型参数错误";
        }else if(!myId.equals(conversation.getMyId())) {
            msg = "账号错误";
        }else if(false){
            //TODO 判断黑名单
        } else {
            //判断完毕，创建对话
            code = "200";
            msg = "新建对话成功";
            try {
                data = privateConversationService.newConversation(myId,toId,type);
            } catch (JsonProcessingException e) {
                code = "500";
                msg = "新建对话失败";
            } catch (NotFoundException e) {
                code = "500";
                msg = e.getMessage();
            }
        }
        return jsonUtil.createResponseModelJsonByString(code,msg, data);
    }

    @PostMapping("list")
    public String getChatList(@RequestBody User user){
        String code = "400";
        String msg;
        String myId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        log.info("获取对话列表，id：{},userId:{}",myId,user.getUserId());
        if(myId == null || user.getUserId() == null) {
            msg = "没有账号信息";
        }else if(!myId.equals(user.getUserId())) {
            msg = "账号不相同";
        } else {
            msg = "获取对话列表失败";
            String conversations;
            try {
                conversations = privateConversationService.getChatList(myId);
            } catch (JsonProcessingException e) {
                return jsonUtil.createResponseModelJsonByString(code,msg,null);
            }
            code = "200";
            msg = "获取对话列表成功";
            log.info("获取对话列表，id：{}，list：{}",myId,conversations);
            return jsonUtil.createResponseModelJsonByString(code,msg,conversations);
        }
        return jsonUtil.createResponseModelJsonByString(code,msg,null);
    }
}