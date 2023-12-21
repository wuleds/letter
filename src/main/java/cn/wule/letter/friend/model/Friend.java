package cn.wule.letter.friend.model;
//汉江师范学院 数计学院 吴乐创建于2023/12/21 18:43

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friend
{
    String userId;
    String userName;
    String friendList;
}