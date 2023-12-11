package cn.wule.letter.log.dao;

import cn.wule.letter.model.log.LogoutLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 注销登录日志
 */
@Mapper
public interface LogoutLogDao
{
    @Insert("insert into logout_log(id,ip,host,code,msg,user_name,user_id,user_info,create_date) " +
            "values(#{id},#{ip},#{host},#{code},#{msg},#{userName},#{userId},#{userInfo},#{createDate})")
    void insertLog(String id, String ip, String host, String code, String msg, String userName, String userId, String userInfo, String createDate);

    @Select("select * from logout_log")
    List<LogoutLog> selectAll();
}