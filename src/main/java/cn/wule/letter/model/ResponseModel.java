package cn.wule.letter.model;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 00:18

import lombok.*;

/**
 * @author 吴乐
 * @version 2023/12/9 00:18
 * 响应对象,用于封装响应数据
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel<T>
{
    String code;
    String msg;
    T data;
}