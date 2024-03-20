package cn.wule.letter.conversation.model;
//汉江师范学院 数计学院 吴乐创建于2024/3/13 0:15

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Conversation
{
    String type;   //对方的类型,可能为private,group,channel
    String chatId; //对话的id
    String myId;   //当前用户的id
    String toId;   //对方的id
}