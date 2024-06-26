package cn.wule.letter.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserPermissionDao
{
    /**
     * 获取用户权限
     */
    List<String> getUserPermissionByUserId(@Param("userId") String userId);
}