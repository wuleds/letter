package cn.wule.letter.connect.service.impl;
//汉江师范学院 数计学院 吴乐创建于2024 4月 11 00:29

import cn.wule.letter.connect.dao.MessageDao;
import cn.wule.letter.connect.model.MessageVo;
import cn.wule.letter.connect.model.UserMessage;
import cn.wule.letter.connect.service.WebSocketService;
import cn.wule.letter.contact.dao.ContactDao;
import cn.wule.letter.model.JwtUserInfo;
import cn.wule.letter.util.JwtUtil;
import cn.wule.letter.util.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
    public boolean persistence(UserMessage userMessage) {
        String chatId = userMessage.getChatId();
        String chatType = messageDao.selectTypeByChatId(chatId);
        String userId = userMessage.getSender();
        String toId = userMessage.getReceiver();
        switch (chatType){
            case "private":
                //黑名单,是否建立了对话
                if(chatId.equals(messageDao.selectChatIdById(userId,toId))){
                    if(isBlackList(userId,toId) && isBlackList(toId,userId)){
                        return false;
                    }
                }
                break;
            case "group":
                //TODO 黑名单，在不在群中
                if(isBlackList(userId,chatId)){
                    return false;
                }
                break;
            case "channel":
                //TODO 黑名单，是否关注频道
                break;
            default:
                return false;
        }
        //持久化消息
        messageDao.saveMessage();

        return true;
    }

    /**
     * 获取当前聊天的记录
     * */
    @Override
    public List<MessageVo> getCurrentMessage(String userId, String chatId) {
        //TODO 获取当前聊天的记录
        //首先确认chatId的类型
        //然后从数据库中获取聊天记录
        return List.of();
    }

    /**
     * 获取未读消息数量
     * */
    @Override
    public List<Integer> getUnreadMessageCount(String userId, String[] chatIds,String[] messageIds) {
        //TODO 获取未读消息数量
        //首先确认chatId的类型
        //然后从数据库中获取最新消息的messageId，然后与传入的messageId进行相减，获取未读消息数量
        //返回的List中的顺序与传入的chatIds一致

        return List.of();
    }

    /**根据id查询是否在黑名单中*/
    private boolean isBlackList(String id, String toId){
        String blackList = contactDao.getBlackList(toId);
        return blackList.contains(id);
    }
}