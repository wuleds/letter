package cn.wule.letter.conversation.dao;

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

    /**更新对话列表*/
    @Update("update chat_list set list = #{list} where user_id = #{userId}")
    boolean updateChatList(String userId,String list);

}