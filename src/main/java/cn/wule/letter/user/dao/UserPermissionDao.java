package cn.wule.letter.user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserPermissionDao
{
    /**
     * 获取用户权限
     */
    @Select("select permission.permission_id" +
            "        from user_group_permission as ugp,user,user_group as ug,permission" +
            "        where user_id = #{userId} and user.user_group_id = ug.user_group_id" +
            "          and ug.user_group_id = ugp.group_id and permission.permission_id = ugp.permission_id")
    List<String> getUserPermissionByUserId(String userId);
}