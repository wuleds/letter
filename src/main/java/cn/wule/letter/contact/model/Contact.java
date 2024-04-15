package cn.wule.letter.contact.model;
//汉江师范学院 数计学院 吴乐创建于2023/12/21 18:43

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contact
{
    String userId;     //账号
    String contactList; //联系人列表
}