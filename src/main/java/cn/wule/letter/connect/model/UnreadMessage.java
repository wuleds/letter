package cn.wule.letter.connect.model;
//汉江师范学院 数计学院 吴乐创建于2024 4月 13 00:53

import lombok.Data;

@Data
public class UnreadMessage
{
    private String chatId;
    private int lastMessageId;
}