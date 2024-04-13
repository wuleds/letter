package cn.wule.letter.conversation.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 私聊对话元数据访问层
 */
@Mapper
public interface PrivateConversationDao {
    /**
     * 根据双方账号查询私聊的chatId
     */
    String selectChatIdById(String oneId,String twoId);

    /**
     * 根据chatId新建私聊表列
     */
    void insertPrivateConversation(@Param("chatId") String chatId,@Param("oneId") String oneId,@Param("twoId") String twoId);

    /**
     * 根据chatId创建消息表
     */
    void createMessageTable(@Param("chatId") String chatId);

    /**查询用户是否有某对话*/
    String getChatOnChatId(String userId,String chatId);
}