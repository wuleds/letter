package cn.wule.letter.connect.service;


import cn.wule.letter.connect.model.MessageVo;
import cn.wule.letter.connect.model.UnreadMessage;
import cn.wule.letter.connect.model.UserMessage;

import java.util.List;

public interface WebSocketService {
    //检查token是否合法
    String checkToken(String jwt);

    /**存储消息*/
    boolean persistence(UserMessage userMessage);

    /**获取当前用户的所有未读消息*/
    List<MessageVo> getCurrentMessage(String userId, String chatId, int lastMessageId);

    /**获取当前用户的所有未读消息的个数*/
    List<UnreadMessage> getUnreadMessageCount(String userId, List<UnreadMessage> unread);

    /**获取chatId的类型*/
    String getChatType(String chatId);

    boolean isBlack(String id, String toId);
}