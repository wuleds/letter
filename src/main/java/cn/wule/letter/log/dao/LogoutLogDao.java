package cn.wule.letter.log.dao;

import cn.wule.letter.model.log.LogoutLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 注销登录日志
 */
@Mapper
public interface LogoutLogDao
{
    @Insert("insert into logout_log(id,ip,host,code,msg,user_name,user_id,user_info) " +
            "values(#{logoutLog.id},#{logoutLog.ip},#{logoutLog.host},#{logoutLog.code},#{logoutLog.msg}," +
            "#{logoutLog.userName},#{logoutLog.userId},#{logoutLog.userInfo})")
    void insertLog(LogoutLog logoutLog);
}