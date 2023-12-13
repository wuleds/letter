package cn.wule.letter.log.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SigninLogDao
{
    @Insert("insert into signin_log(id, user_id, user_name, signin_date, ip, host, code, msg) " +
            "values(#{id}, #{userId}, #{userName}, #{signinDate}, #{ip}, #{host}, #{code}, #{msg})")
    void insertLog(String id, String userId, String userName, String signinDate, String ip, String host, String code, String msg);
}