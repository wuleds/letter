package cn.wule.letter.log.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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
    @Insert("insert into request_log(id,ip,host,uri,code,msg,user_name,user_id,user_info) " +
            "values(#{id},#{ip},#{host},#{uri},#{code},#{msg},#{userName},#{userId},#{userInfo})")
    void insertLog(String id,String ip,String host,String uri,String code,String msg,String userName,String userId,String userInfo);
}