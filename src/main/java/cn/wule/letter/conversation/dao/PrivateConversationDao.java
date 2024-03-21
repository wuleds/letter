package cn.wule.letter.conversation.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 私聊对话元数据访问层
 */
@Mapper
public interface PrivateConversationDao {
    /**
     * 根据双方账号查询私聊的chatId
     * @return chatId
     */
    @Select("select chat_id from private_conversation " +
            "where ((one_id = #{oneId} and two_id = #{twoId}) or (one_id = #{twoId} and two_id = #{oneId})) and del_flag")
    String selectChatIdById(String oneId,String twoId);

    /**
     * 根据chatId新建私聊表列
     * @param chatId 会话id
     * @param oneId 用户id
     * @param twoId 用户id
     */
    @Select("insert into private_conversation(chat_id, create_date, update_date, one_id, two_id, del_flag) " +
            "values(#{chatId}, now(), now(), #{oneId}, #{twoId}, 0)")
    boolean insertPrivateConversation(String chatId, String oneId, String twoId);
}