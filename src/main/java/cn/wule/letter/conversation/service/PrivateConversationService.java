package cn.wule.letter.conversation.service;

import cn.wule.letter.conversation.model.Conversation;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.ibatis.javassist.NotFoundException;

import java.util.HashSet;

/**
 * 私聊对话业务逻辑层
 */
public interface PrivateConversationService {
    /**
     * 创建对话
     * @return chatId
     */
    String newConversation(String myId, String toId,String type) throws JsonProcessingException, NotFoundException;


    /**获取对话列表*/
    String getChatList(String userId);
}