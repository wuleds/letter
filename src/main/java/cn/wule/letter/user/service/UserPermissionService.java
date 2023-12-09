package cn.wule.letter.user.service;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 18:17

import java.util.List;

public interface UserPermissionService {
    List<String> getUserPermissionByUserId(String userId);
}