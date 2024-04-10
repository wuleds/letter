package cn.wule.letter.connect.service;


import cn.wule.letter.connect.model.MessageVo;
import cn.wule.letter.connect.model.UserMessage;

import java.util.List;

public interface WebSocketService {
    String checkToken(String jwt);

    boolean persistence(UserMessage userMessage);

    List<MessageVo> getCurrentMessage(String userId,String chatId);

    List<Integer> getUnreadMessageCount(String userId,String[] chatIds,String[] messageIds);
}