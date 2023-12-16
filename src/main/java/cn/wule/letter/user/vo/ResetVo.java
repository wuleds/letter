package cn.wule.letter.user.vo;
//汉江师范学院 数计学院 吴乐创建于2023/12/16 18:40

import lombok.Data;

@Data
public class ResetVo
{
    String longUrl;
    String password;
    String secondPassword;
}