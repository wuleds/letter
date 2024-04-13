package cn.wule.letter.connect.model;
//汉江师范学院 数计学院 吴乐创建于2024 4月 10 02:29

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServerMessage
{
    /**服务器消息类型
     * 0.心跳包,text为"pong"
     * 1.验证失败通知，text为"error"
     * 2.系统通知, text为通知内容
     * 3.操作提示，text为提示内容
     * 4.未读消息数量，text为List<UnreadMessage>
     * 5.未读消息,text为List<MessageVo>
     * 6.删除消息，text为messageId,chatId
     * */
    private String type;
    private String text;
    /**当消息类型为5时，需要以下两个字段*/
    private String chatId;
    /**对话类型
     * private 私聊
     * group 群聊
     * channel 频道
     * */
    private String chatType;
}