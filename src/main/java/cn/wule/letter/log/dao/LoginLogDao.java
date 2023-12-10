package cn.wule.letter.log.dao;

import cn.wule.letter.model.log.LoginLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志
 */
@Mapper
public interface LoginLogDao
{
    @Insert("insert into login_log(id, user_id, user_name, login_date, ip, host,code,msg) " +
            "values(#{loginLog.id}, #{loginLog.userId}, #{loginLog.userName}, " +
            "#{loginLog.loginDate}, #{loginLog.ip}, #{loginLog.host},#{loginLog.code},#{loginLog.msg})")
    void insertLog(LoginLog loginLog);
}