package cn.wule.letter.contact.model;
//汉江师范学院 数计学院 吴乐创建于2023/12/21 19:45

import lombok.Data;

@Data
public class ContactRequest
{
    String id;           //请求id
    String fromUserId;  //请求发送者
    String toUserId;     //请求接收者
    String userName;     //请求发送者的用户名
    String userPhoto;    //请求发送者的头像
    String info;         //请求信息
}