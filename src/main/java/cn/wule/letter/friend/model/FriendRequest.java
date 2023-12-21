package cn.wule.letter.friend.model;
//汉江师范学院 数计学院 吴乐创建于2023/12/21 19:45

import lombok.Data;

@Data
public class FriendRequest
{
    String fromUserId;
    String toUserId;
    String info;
}