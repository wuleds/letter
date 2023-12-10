package cn.wule.letter.log.dao;

import cn.wule.letter.model.log.RequestLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

//汉江师范学院 数计学院 吴乐创建于2023/12/9 02:24

/**
 * 请求过滤日志持久层
 */
@Mapper
public interface RequestLogDao
{
    /**
     * 请求过滤日志
     */
    @Insert("insert into request_log(id,ip,host,uri,code,msg,user_name,user_id,user_info,create_date) " +
            "value(#{requestLog.id},#{requestLog.ip},#{requestLog.host},#{requestLog.uri},#{requestLog.code}," +
            "#{requestLog.msg},#{requestLog.userName},#{requestLog.userId},#{requestLog.userInfo}," +
            "#{requestLog.createDate})")
    void insertLog(RequestLog requestLog);
}