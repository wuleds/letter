package cn.wule.letter.log.dao;

import cn.wule.letter.model.log.LoginLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 登录日志
 */
@Mapper
public interface LoginLogDao
{
    @Insert("insert into login_log(id, user_id, user_name, login_date, ip, host,code,msg) " +
            "values(#{id},#{userId},#{userName},#{loginDate},#{ip},#{host},#{code},#{msg})")
    void insertLog(String id, String userId, String userName, String loginDate, String ip, String host, String code, String msg);

    @Select("select * from login_log")
    List<LoginLog> selectAll();
}