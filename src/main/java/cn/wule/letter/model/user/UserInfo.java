package cn.wule.letter.model.user;
//汉江师范学院 数计学院 吴乐创建于2023/12/10 1:51

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户信息表
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserInfo
{
    String userId;   //用户id
    String userName; //用户名
    String userPhoto; //用户头像
    String userTalk;  //用户签名
    String userSex;  //用户性别,私密
    String userEmail; //用户邮箱，私密
    String userPhone; //用户电话，私密
    String userAddress; //用户地址，私密
    Date userBirthday; //用户生日，私密
}