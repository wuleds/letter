package cn.wule.letter.contact.model;
//汉江师范学院 数计学院 吴乐创建于2023/12/21 18:43

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contact
{
    String userId;     //账号
    String userName;   //用户名
    String friendList; //好友列表
}