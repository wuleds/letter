package cn.wule.letter.model.log;
//汉江师范学院 数计学院 吴乐创建于2023/12/11 1:14

import lombok.Builder;
import lombok.Data;

/**
 * 注册日志
 */
@Data
@Builder
public class SigninLog
{
    String id;
    String userId;
    String userName;
    String signinDate;
    String ip;
    String host;
    String code;
    String msg;
}