package cn.wule.letter.contact.model;
//汉江师范学院 数计学院 吴乐创建于2024/3/10 23:30


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 联系人列表中联系人的信息。
 */
@Data
@NoArgsConstructor
public class ContactInfo
{
    String contactId;   //账号
    String contactName; //用户名
    String contactPhoto;   //用户头像
}