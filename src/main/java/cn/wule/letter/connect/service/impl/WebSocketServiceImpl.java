package cn.wule.letter.connect.service.impl;
//汉江师范学院 数计学院 吴乐创建于2024 4月 11 00:29

import cn.wule.letter.channel.dao.ChannelDao;
import cn.wule.letter.connect.dao.MessageDao;
import cn.wule.letter.connect.model.MessageVo;
import cn.wule.letter.connect.model.UnreadMessage;
import cn.wule.letter.connect.model.UserMessage;
import cn.wule.letter.connect.service.WebSocketService;
import cn.wule.letter.contact.dao.ContactDao;
import cn.wule.letter.conversation.dao.PrivateConversationDao;
import cn.wule.letter.group.dao.GroupDao;
import cn.wule.letter.model.JwtUserInfo;
import cn.wule.letter.util.JwtUtil;
import cn.wule.letter.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class WebSocketServiceImpl implements WebSocketService
{
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private MessageDao messageDao;
    @Resource
    private ContactDao contactDao;
    @Resource
    private GroupDao groupDao;
    @Resource
    private ChannelDao channelDao;
    @Resource
    private PrivateConversationDao privateConversationDao;
    @Resource
    private ObjectMapper om;

    /**
     * 检查jwt真实性*/
    @Override
    public String checkToken(String jwt) {
        JwtUserInfo userInfo = jwtUtil.verifyJWT(jwt);
        //检查jwt是否合法
        if (userInfo != null){
            //检查redis中该userId是否有jwt
            if(redisUtil.isJwtExist(userInfo)){
                //检查redis中的该userId的jwt是否与传入的jwt相同
                if(redisUtil.getJwt(userInfo).equals(jwt)){
                    return userInfo.getUserId();
                }
            }
        }
        return null;
    }

    /**
     * 持久化消息
     * */
    @Override
    public boolean persistence(UserMessage userMessage) throws JsonProcessingException {
        String chatId = userMessage.getChatId();
        String chatType = messageDao.selectTypeByChatId(chatId);
        String userId = userMessage.getSender();
        String toId = userMessage.getReceiver();
        String image = userMessage.getImages();

        switch (chatType){
            case "private":
                //黑名单,是否建立了对话
                if(chatId.equals(messageDao.selectChatIdById(userId,toId))){
                    if( !isBlack(userId,toId) && !isBlack(toId,userId)){
                        //持久化消息
                        switch (userMessage.getType()){
                            case "2":
                                //2：文本，图片，视频
                                messageDao.savePrivateMessage(chatId,userId,toId,userMessage.getType(),userMessage.getText(), image,userMessage.getVideo(),null,null,userMessage.getReplyStatus(),userMessage.getReplyMessageId());
                                return true;
                            case "3":
                                //文件,文本
                                messageDao.savePrivateMessage(chatId,userId,toId,userMessage.getType(),userMessage.getText(),null,null,null,userMessage.getFile(),userMessage.getReplyStatus(),userMessage.getReplyMessageId());
                                return true;
                            case "4":
                                //音频
                                messageDao.savePrivateMessage(chatId,userId,toId,userMessage.getType(),null,null,null,userMessage.getAudio(),null,userMessage.getReplyStatus(),userMessage.getReplyMessageId());
                                return true;
                            default:
                                break;
                        }
                        messageDao.savePrivateMessage(chatId,userId,toId,userMessage.getType(),userMessage.getText(), userMessage.getImages(),userMessage.getVideo(),userMessage.getAudio(),userMessage.getFile(),userMessage.getReplyStatus(),userMessage.getReplyMessageId());
                        return true;
                    }
                }
            case "group":
                //黑名单， 在不在群中
                if(!isBlack(userId,chatId)){
                    if(groupDao.getUserPositionInGroup(chatId,userId) != null){
                        //持久化消息
                        messageDao.saveGroupMessage(chatId,userId,userMessage.getType(),userMessage.getText(), userMessage.getImages(),userMessage.getVideo(),userMessage.getAudio(),userMessage.getFile(),userMessage.getReplyStatus(),userMessage.getReplyMessageId());
                        return true;
                    }
                }
                break;
            case "channel":
                //黑名单，是否关注了频道
                if(!isBlack(userId,chatId)){
                    if(channelDao.getAttention(userId,chatId) != null){
                        //持久化消息
                        messageDao.saveChannelMessage(chatId,userId,userMessage.getType(),userMessage.getText(), userMessage.getImages(),userMessage.getVideo(),userMessage.getAudio(),userMessage.getFile(),userMessage.getReplyMessageId());
                        return true;
                    }
                }
            default:
                break;
        }
        return false;
    }

    /**获取当前聊天的记录*/
    @Override
    public List<MessageVo> getCurrentMessage(String userId, String chatId, int lastMessageId) {
        String type = messageDao.getChatIdType(chatId);
        List<MessageVo> messages = null;
        switch (type){
            case "private":
                if(privateConversationDao.getChatOnChatId(userId,chatId).equals(chatId)) {
                    messages = messageDao.selectPrivateUnreadMessage(chatId,lastMessageId);
                }
                break;
            case "group":
                //查询用户是否处于该组
                if(groupDao.getUserPositionInGroup(chatId,userId)  != null) {
                    messages = messageDao.selectGroupUnreadMessage(chatId,lastMessageId);
                }
                break;
            case "channel":
                //确认用户关注了群组
                if(channelDao.getAttention(userId,chatId) != null) {
                    messages = messageDao.selectChannelUnreadMessage(chatId,lastMessageId);
                }
                break;
            default:break;
        }
        return messages;
    }

    /** 获取未读消息数量*/
    @Override
    public List<UnreadMessage> getUnreadMessageCount(String userId, List<UnreadMessage> unreadList) {
        //首先确认chatId的类型
        //然后从数据库中获取最新消息的messageId，然后与传入的messageId进行相减，获取未读消息数量
        List<UnreadMessage> list = new ArrayList<>();
        unreadList.forEach(unread ->{
            String chatId = unread.getChatId();
            String type = messageDao.getChatIdType(chatId);
            switch (type) {
                case "private":
                    if(privateConversationDao.getChatOnChatId(userId,chatId).equals(chatId)) {
                        unread.setLastMessageId(messageDao.getPrivateLastMessageId(chatId)- unread.getLastMessageId());
                    }
                    break;
                case "group":
                    //查询用户是否处于该组
                    if(groupDao.getUserPositionInGroup(chatId,userId)  != null) {
                        unread.setLastMessageId(messageDao.getGroupLastMessageId(chatId)- unread.getLastMessageId());
                    }
                    break;
                case "channel":
                    //确认用户关注了群组
                    if(channelDao.getAttention(userId,chatId) != null) {
                        unread.setLastMessageId(messageDao.getChannelLastMessageId(chatId) - unread.getLastMessageId());
                    }
                    break;
                default:break;
            }
            list.add(unread);
        });
        return list;
    }

    @Override
    public String getChatType(String chatId) {
        return messageDao.getChatIdType(chatId);
    }

    /**根据id查询是否在黑名单中*/
    public boolean isBlack(String id, String toId){
        String blackList = contactDao.getBlackList(toId);
        if(blackList == null){
            return false;
        }
        return blackList.contains(id);
    }
}