package cn.wule.letter.model.user;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 18:25

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User
{
    String userId;
    String userName;
    String userPassword;
    String userGroupId;
    String delFlag;
}