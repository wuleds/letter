package cn.wule.letter.contact.model;
//汉江师范学院 数计学院 吴乐创建于2023/12/21 19:54

import lombok.Data;

/**
 * 好友请求处理
 * */
@Data
public class ContactRequestHandle
{
    String requestId;   //请求id
    int status;        //处理结果,0:未处理,1:同意,2:忽略
}