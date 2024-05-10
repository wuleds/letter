package cn.wule.letter.story.model;
//汉江师范学院 数计学院 吴乐创建于2024 5月 10 22:19

import lombok.Data;

@Data
public class Story
{
    String id;
    String senderId;
    String text;
    String image;
    String video;
    String likeCount;
    String liker;
    String comment;
    String createDate;
}