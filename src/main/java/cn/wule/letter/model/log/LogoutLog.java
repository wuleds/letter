package cn.wule.letter.model.log;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 22:04

import lombok.*;

/**
 * 注销登录请求日志
 */
@Data
@Builder
public class LogoutLog {
    String id;
    String ip;
    String host;
    String code;
    String msg;
    String userName;
    String userId;
    String userInfo;
    String createDate;
}