package cn.wule.letter.user.dao;

import cn.wule.letter.model.user.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 用户信息表
 */
@Mapper
public interface UserInfoDao {
    /**
     * 根据用户id获取用户信息
     */
    @Select("select * from user_info where user_id = #{userId} and del_flag = 1")
    UserInfo getUserInfoById(String userId);

    /**
     * 添加用户信息
     * @param userId 用户id
     */
    @Insert("insert into user_info(user_id,del_flag) values(#{userId},1)")
    void addUserInfoById(String userId);

    /**
     * 更新用户信息
     */
    @Update("update user_info set user_sex = #{userSex},user_birthday = #{userBirthday},user_name = #{userName}," +
            "user_address = #{userAddress},user_phone = #{userPhone}," +
            "user_email = #{userEmail} where user_id = #{userId}")
    void updateUserInfo(String userId,String userName,String userSex,String userBirthday,String userAddress,String userPhone,String userEmail);

    /**删除用户信息*/
    @Update("update user_info set del_flag = 0 where user_id = #{userId}")
    void deleteUserInfoById(String userId);

    @Select("select user_photo from user_info where user_id = #{userId}")
    String getUserInfoPhoto(String userId);
}