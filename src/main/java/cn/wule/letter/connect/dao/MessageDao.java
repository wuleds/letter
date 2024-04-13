package cn.wule.letter.connect.dao;

import cn.wule.letter.connect.model.MessageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    @Select("select type from chat_id_type where chat_id = #{chatId} and del_flag = 0")
    String getChatIdType(String chatId);

    /**存储私聊消息*/
    boolean savePrivateMessage(String chatId,String sender, String receiver, String type, String text, String image, String video, String audio, String file,String replyStatus,String replyMessageId);

    /**存储群聊消息*/
    boolean saveGroupMessage(String chatId,String sender, String type, String text, String image, String video, String audio, String file,String replyStatus,String replyMessageId);

    /**存储用户发送的频道消息的评论*/
    boolean saveChannelMessage(String chatId,String sender, String type, String text, String image, String video, String audio, String file,String replyMessageId);

    /**查询私聊最新消息id
     * 这里不会有sql注入的问题，因为chatId会查询type，假的chatId查不出东西，到不了这里。
     * */
    int getPrivateLastMessageId(String chatId);

    /**查询群组最新消息id*/
    int getGroupLastMessageId(String chatId);

    /**查询频道最新消息id*/
    int getChannelLastMessageId(String chatId);

    /**查询私聊的未读消息*/
    List<MessageVo> selectPrivateUnreadMessage(String chatId,int lastMessageId);

    /**查询群组的未读消息*/
    List<MessageVo> selectGroupUnreadMessage(String chatId,int lastMessageId);

    /**查询频道的未读消息*/
    List<MessageVo> selectChannelUnreadMessage(String chatId,int lastMessageId);
}