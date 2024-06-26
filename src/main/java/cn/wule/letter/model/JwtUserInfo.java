package cn.wule.letter.model;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 01:23

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtUserInfo
{
    String userId;
    String userName;
    Map<String,String> userInfo;
}