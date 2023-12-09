package cn.wule.letter.log.dao;
//汉江师范学院 数计学院 吴乐创建于2023/12/10 2:02

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户注册日志表
 */
@Mapper
public interface UserSigninLogDao
{
    /**
     * 添加用户注册日志
     * @param userId 用户id
     * @param id 日志id
     */
    @Insert("insert into user_signin_log(id,user_id) values(#{id},#{userId})")
    void addUserSigninLog(String id,String userId);
}