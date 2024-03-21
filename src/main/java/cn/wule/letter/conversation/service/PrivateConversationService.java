package cn.wule.letter.conversation.service;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 私聊对话业务逻辑层
 */
public interface PrivateConversationService {
    /**
     * 创建对话
     * @return chatId
     */
    String newConversation(String myId, String toId,String type) throws JsonProcessingException;
}