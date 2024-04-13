package cn.wule.letter.connect.model;
//汉江师范学院 数计学院 吴乐创建于2024 4月 11 02:42

import lombok.Data;

import java.util.List;

@Data
public class MessageVo
{
    private String messageId;
    private String senderId;
    private String receiverId;
    private String type;
    private String text;
    private List<String> image;
    private String video;
    private String audio;
    private String file;
    private String replyStatus;
    private String replyId;
    private String createDate;
    private String updateDate;
}