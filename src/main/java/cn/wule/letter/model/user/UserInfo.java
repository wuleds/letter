package cn.wule.letter.model.user;
//汉江师范学院 数计学院 吴乐创建于2023/12/10 1:51

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserInfo
{
    String userId;
    String userSex;
    String userEmail;
    String userPhone;
    String userAddress;
    Date userBirthday;
}