package cn.wule.letter.conversation.service.impl;
//汉江师范学院 数计学院 吴乐创建于2024/3/21 23:44

import cn.wule.letter.conversation.dao.ChatListDao;
import cn.wule.letter.conversation.dao.PrivateConversationDao;
import cn.wule.letter.conversation.model.Conversation;
import cn.wule.letter.conversation.service.PrivateConversationService;
import cn.wule.letter.group.dao.GroupDao;
import cn.wule.letter.user.dao.UserInfoDao;
import cn.wule.letter.util.UUIDUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class PrivateConversationServiceImpl implements PrivateConversationService
{
    @Resource
    private PrivateConversationDao privateConversationDao;
    @Resource
    private ChatListDao chatListDao;
    @Resource
    private ObjectMapper om;
    @Resource
    private GroupDao groupDao;
    @Resource
    private UserInfoDao userInfoDao;

    /**
     * 创建对话
     * 私聊,查询chatId，或新建对话，然后存进双方的对话列表。
     * 群组，将群组的groupId存进用户的对话列表,并返回groupId。
     * 频道，将频道的channelId存进用户的对话列表，并返回channelId。
     * @return chatId
     */
    @Override
    @Transactional
    public String newConversation(String myId, String toId,String type) throws JsonProcessingException, NotFoundException {
        String chatId = "";
        switch (type) {
            case "private":
                //先查询是否已经存在对话，有则用现成的，没有则创建新对话
                chatId = privateConversationDao.selectChatIdById(myId,toId);
                if(chatId == null) {
                    //创建新的chatId和新的对话
                    chatId = UUIDUtil.getUUID();
                    //存入私聊信息表
                    privateConversationDao.insertPrivateConversation(chatId,myId,toId);
                    //根据chatId创建对话消息表。
                    privateConversationDao.createMessageTable(chatId);
                    //设置私聊id的类型
                    chatListDao.setPrivateChatIdType(chatId);
                }

                //将对话存进双方的对话列表,为什么要存，因为有可能有一方已经把对话删除了。
                String setJson1 = chatListDao.selectChatList(myId);
                String setJson2 = chatListDao.selectChatList(toId);
                HashSet<String> oneSet;
                HashSet<String> twoSet;
                try {
                    oneSet = om.readValue(setJson1, new TypeReference<HashSet<String>>() {});
                } catch (JsonProcessingException | IllegalArgumentException e ) {
                    oneSet = new HashSet<String>();
                }
                try {
                    twoSet = om.readValue(setJson2, new TypeReference<HashSet<String>>() {});
                } catch (JsonProcessingException | IllegalArgumentException e) {
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
                //检查群组是否存在
                //检查是否是群组成员
                //将群组号存进用户的对话列表
                chatId = toId;
                if(groupDao.getGroupNameById(chatId) == null){
                    throw new NotFoundException("群组不存在");
                };
                if(groupDao.getUserPositionInGroup(chatId,myId) == null){
                    throw new NotFoundException("不是群组成员");
                }
                String setJson = chatListDao.selectChatList(myId);
                HashSet<String> mySet;
                try {
                    mySet = om.readValue(setJson, new TypeReference<HashSet<String>>() {});
                } catch (JsonProcessingException | IllegalArgumentException e ) {
                    mySet = new HashSet<String>();
                }
                //将chatId存进双方的对话列表，然后存进数据库
                if(mySet.add(chatId)) {
                    chatListDao.updateChatList(myId, om.writeValueAsString(mySet));
                }
                break;
            case "channel":
                //TODO 检查频道是否存在
                if(true){
                    throw new NotFoundException("频道不存在");
                };
                //检查是否是关注的频道
                //将频道号存进用户的对话列表
                chatId = toId;
        }
        Conversation conversation = Conversation.builder().chatId(chatId).type(type).myId(myId).toId(toId).build();
        log.info("新建对话：{}",conversation);
        return om.writeValueAsString(conversation);
    }

    @Override
    public String  getChatList(String userId) throws JsonProcessingException {
        //获取对话列表
        String chatListJson = chatListDao.selectChatList(userId);
        //转换为set类型,这里只存储了chatId。
        HashSet<String> chatSet = om.readValue(chatListJson, new TypeReference<HashSet<String>>() {});
        //转化为list类型
        List<Conversation> chatList = new ArrayList<>();
        for (String chatId : chatSet) {
            Conversation conversation = new Conversation();
            //获取对话类型
            String type = chatListDao.selectTypeByChatId(chatId);
            //如果是私聊，则获取对方id，否则直接用chatId。
            String toId = type.equals("private")?chatListDao.getPrivateChat(chatId,userId):chatId;
            switch (type) {
                case "private":
                    //获取对方的信息
                    conversation.setPhoto(userInfoDao.getUserInfoPhoto(toId));
                    conversation.setName(userInfoDao.getUserInfoById(toId).getUserName());
                    break;
                case "group":
                    //获取群组的信息
                    conversation.setName(groupDao.getGroupNameById(toId));
                    conversation.setPhoto(groupDao.getGroupPhoto(toId));
                    break;
                case "channel":
                    //获取频道的信息
                    conversation.setName(groupDao.getChannelName(toId));
                    conversation.setPhoto(groupDao.getChannelPhoto(toId));
                    break;
            }
            conversation.setChatId(chatId);
            conversation.setType(type);
            conversation.setMyId(userId);
            conversation.setToId(toId);
            chatList.add(conversation);
        }
        return om.writeValueAsString(chatList);
    }
}