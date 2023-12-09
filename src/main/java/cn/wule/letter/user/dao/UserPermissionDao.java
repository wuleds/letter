package cn.wule.letter.user.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserPermissionDao
{
    /**
     * 获取用户权限
     */
    List<String> getUserPermissionByUserId(String userId);
}