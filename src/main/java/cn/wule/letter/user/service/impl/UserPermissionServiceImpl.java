package cn.wule.letter.user.service.impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 18:18

import cn.wule.letter.user.dao.UserPermissionDao;
import cn.wule.letter.user.service.UserPermissionService;
import jakarta.annotation.Resource;

import java.util.List;

/**
 * 用户权限查询服务层实现类
 */
public class UserPermissionServiceImpl implements UserPermissionService {
    @Resource
    UserPermissionDao userPermissionDao;
    @Override
    public List<String> getUserPermissionByUserId(String userId) {
        return userPermissionDao.getUserPermissionByUserId(userId);
    }
}