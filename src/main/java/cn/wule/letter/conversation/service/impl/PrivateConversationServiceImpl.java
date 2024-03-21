package cn.wule.letter.conversation.service.impl;
//汉江师范学院 数计学院 吴乐创建于2024/3/21 23:44

import cn.wule.letter.conversation.dao.ChatListDao;
import cn.wule.letter.conversation.dao.PrivateConversationDao;
import cn.wule.letter.conversation.service.PrivateConversationService;
import cn.wule.letter.util.UUIDUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class PrivateConversationServiceImpl implements PrivateConversationService
{
    @Resource
    private PrivateConversationDao privateConversationDao;
    @Resource
    private ChatListDao chatListDao;
    @Resource
    private ObjectMapper om;

    /**
     * 创建对话
     * 私聊,查询chatId，或新建对话，然后存进双方的对话列表。
     * 群组，将群组的groupId存进用户的对话列表,并返回groupId。
     * 频道，将频道的channelId存进用户的对话列表，并返回channelId。
     * @return chatId
     */
    public String newConversation(String myId, String toId,String type) throws JsonProcessingException {
        String chatId;
        switch (type) {
            case "private":
                //获取chatId
                chatId = privateConversationDao.selectChatIdById(myId,toId);
                if(chatId == null) {
                    //创建新的chatId和新的对话
                    chatId = UUIDUtil.getUUID();
                    privateConversationDao.insertPrivateConversation(chatId,myId,toId);
                }
                //将对话存进双方的对话列表
                String setJson1 = chatListDao.selectChatList(myId);
                String setJson2 = chatListDao.selectChatList(toId);
                HashSet<String> oneSet;
                HashSet<String> twoSet;
                try {
                    oneSet = om.readValue(setJson1, new TypeReference<HashSet<String>>() {});
                } catch (JsonProcessingException e) {
                    oneSet = new HashSet<String>();
                }
                try {
                    twoSet = om.readValue(setJson2, new TypeReference<HashSet<String>>() {});
                } catch (JsonProcessingException e) {
                    twoSet = new HashSet<String>();
                }
                //将chatId存进双方的对话列表，然后存进数据库
                if(oneSet.add(chatId)) {
                    chatListDao.updateChatList(myId,om.writeValueAsString(oneSet));
                }
                if(twoSet.add(chatId)){
                    chatListDao.updateChatList(toId, om.writeValueAsString(twoSet));
                }
                break;
            case "group":
                return "group";
            case "channel":
                return "channel";
            default:
                return null;
        }
        return chatId;
    }



    public String getChatList(String userId){
        return ";";
    }
}