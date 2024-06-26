package cn.wule.letter.conversation.model;
//汉江师范学院 数计学院 吴乐创建于2024/3/13 0:15

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Conversation
{
    String type;   //对话的类型,可能为private,group,channel
    String chatId; //对话的id
    String myId;   //当前用户的id
    String toId;   //对方的id
    String name;   //对方的名字
    String photo;  //对方的头像
}