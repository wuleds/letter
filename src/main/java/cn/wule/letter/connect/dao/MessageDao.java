package cn.wule.letter.connect.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MessageDao {

    /**根据双方id查询私聊id*/
    @Select("select chat_id" +
            "        from private_conversation" +
            "        where ((one_id = #{oneId} and two_id = #{twoId}) or (one_id = #{twoId} and two_id = #{oneId}))" +
            "          and del_flag = 0;")
    String selectChatIdById(String oneId,String twoId);

    /**查询chatId的type*/
    @Select("select type from chat_id_type where chat_id = #{chatId} and del_flag = 0")
    String selectTypeByChatId(String chatId);

    /**存储消息*/
    String saveMessage(String chatId,String chatType,String sender, String receiver, String type, String text, String image, String video, String audio, String file,String replyStatus,String replyMessageId);
}