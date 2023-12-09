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
    @Select("select * from user_info where user_id = #{userId}")
    UserInfo getUserInfoById(String userId);

    @Insert("insert into user_info(user_id) values(#{userId})")
    void addUserInfo(String userId);

    @Update("update user_info set user_sex = #{userInfo.userSex},user_birthday = #{userInfo.userBirthday}," +
            "user_address = #{userInfo.userAddress},user_phone = #{userInfo.userPhone}," +
            "user_email = #{userInfo.userEmail} where user_id = #{userInfo.userId}")
    boolean updateUserInfo(UserInfo userInfo);
}