package cn.wule.letter.friend.model;
//汉江师范学院 数计学院 吴乐创建于2023/12/21 19:54

import lombok.Data;

/**
 * 好友请求处理
 * */
@Data
public class FriendRequestHandle
{
    String fromUserId; //好友请求发送者
    String toUserId;   //好友请求处理者
    int status;        //处理结果
}