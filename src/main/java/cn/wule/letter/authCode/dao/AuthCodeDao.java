package cn.wule.letter.authCode.dao;
//汉江师范学院 数计学院 吴乐创建于2023/12/12 01:44

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AuthCodeDao
{
    /**
     * 保存验证码
     */
    @Insert("insert into auth_code(id,method,contact,code,date,flag) " +
            "values(#{id},#{method},#{contact},#{code},#{time},1)")
    void saveAuthCode(String id,String method, String contact, String code,String time);

    /**
     * 删除该联系方式的所有验证码
     */
    @Update("update auth_code set flag=0 where contact=#{contact}")
    void deleteAuthCode(String contact);

    /**
     * 获取最新的有效的验证码
     */
    @Select("select code from auth_code " +
            "where method=#{method} and contact=#{contact} and flag=1 " +
            "and id = (select id from auth_code order by date desc limit 1)")
    String getAuthCode(String method, String contact);
}