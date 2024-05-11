package cn.wule.letter.story.model;
//汉江师范学院 数计学院 吴乐创建于2024 5月 11 20:49

import lombok.Data;

@Data
public class Comment
{
    String storyId;
    String senderId;
    String text;
}