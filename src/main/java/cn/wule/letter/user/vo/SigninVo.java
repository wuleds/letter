package cn.wule.letter.user.vo;
//汉江师范学院 数计学院 吴乐创建于2023/12/12 01:24

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SigninVo
{
    String userName;
    String password;
    String secondPassword;
    String method; //验证方法，可能是邮箱，或者手机号
    String code; //验证码
    String contact;//联系方式，可能是邮箱，或者手机号
}