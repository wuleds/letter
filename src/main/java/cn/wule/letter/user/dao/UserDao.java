package cn.wule.letter.user.dao;
//汉江师范学院 数计学院 吴乐创建于2023/12/9 23:08

import cn.wule.letter.model.user.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDao
{
    /**
     * 添加普通用户
     * @param userId 用户id
     * @param userName 用户名
     * @param userPassword 用户密码
     */
    @Insert("insert into user(user_id,user_name,user_password,user_group_id) values(#{userId},#{userName},#{userPassword},'3')")
    void addNormalUser(String userId ,String userName,String userPassword);

    /**
     * 根据用户id获取用户信息
     * @param userId 用户id
     * @return User
     */
    @Select("select * from user where user_id = #{userId}")
    User getUserById(String userId);

    /**
     * 查询该用户是否存在，根据用户id
     */
    @Select("select count(*) from user where user_id = #{userId}")
    int queryUserByUserId(String userId);

    @Select("select user_password from user where user_id = #{userId}")
    String checkUser(String userId);

    /**
     * 修改用户密码
     */
    @Update("update user set user_password = #{password} where user_id = #{userId}")
    User updateUserPassword(String userId,String password);

    /**
     * 修改用户名
     */
    @Update("update user set user_name = #{userName} where user_id = #{userId}")
    User updateUserName(String userId,String userName);
}