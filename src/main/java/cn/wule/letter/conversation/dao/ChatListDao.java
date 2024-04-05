package cn.wule.letter.conversation.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 对话列表数据访问层
 */
@Mapper
public interface ChatListDao {
    /**
     * 获取对话列表
     */
    @Select("select list from chat_list where user_id = #{userId} and del_flag = 0")
    String selectChatList(String userId);

    String getPrivateChat(String chatId,String userId);

    /**更新对话列表*/
    @Update("update chat_list set list = #{list} where user_id = #{userId}")
    boolean updateChatList(String userId,String list);

    /**设置私聊的chatId的类型*/
    @Insert("insert into chat_id_type(chat_id,type,del_flag) values(#{chatId},'private',0)")
    boolean setPrivateChatIdType(String chatId);

    /**查询chatId的type*/
    @Select("select type from chat_id_type where chat_id = #{chatId} and del_flag = 0")
    String selectTypeByChatId(String chatId);
}