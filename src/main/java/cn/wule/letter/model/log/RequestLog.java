package cn.wule.letter.model.log;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 2:45

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestLog implements LogBase {
    String id;
    String ip;
    String host;
    String code;
    String msg;
    String uri;
    String userName;
    String userId;
    String userInfo;
}