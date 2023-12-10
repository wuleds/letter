package cn.wule.letter.model.log;
//汉江师范学院 数计学院 吴乐创建于2023/12/11 0:52

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录日志
 */
@Data
@Builder
public class LoginLog {
    String id;
    String userId;
    String userName;
    String loginDate;
    String ip;
    String host;
    String code;
    String msg;
}