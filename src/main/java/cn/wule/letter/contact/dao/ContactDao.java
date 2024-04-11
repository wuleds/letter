package cn.wule.letter.contact.dao;

import cn.wule.letter.contact.model.Contact;
import cn.wule.letter.contact.model.ContactRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 联系人模块Dao层
 */
@Mapper
public interface ContactDao
{
    /**为新注册的用户添加联系人表列*/
    @Insert("insert into contact (user_id,user_name,contact_list) values (#{userId},#{userName},'')")
    void addContactList(String userId, String userName);

    /**更新用户的联系人列表*/
    @Update("update contact set contact_list = #{contactSetJson}  where user_id = #{userId}")
    boolean setContact(String userId, String contactSetJson);

    /** 添加联系人请求*/
    @Insert("insert into contact_request (id,from_user_id,to_user_id,info,create_date,update_date) values (#{id},#{fromUserId},#{toUserId},#{info},#{createDate},#{createDate})")
    void addAddContact(String id, String fromUserId, String toUserId, String info, String createDate);

    /**处理联系人请求*/
    @Update("update contact_request set status = #{status},update_date = #{updateDate} where from_user_id = #{fromUserId} and to_user_id = #{toUserId}")
    void handleAddContact(String fromUserId, String toUserId, int status, String updateDate);

   /**获取所有的添加联系人请求*/
    @Select("select * from contact_request where to_user_id = #{userId} and status = 0")
    List<ContactRequest> getAddContactList(String userId);

    /**根据id获取对应的添加联系人请求*/
    @Select("select * from contact_request where id = #{requestId} and to_user_id = #{userId} and status = 0")
    ContactRequest getAddContactById(String requestId,String userId);

    /**获取联系人请求*/
    @Select("select from_user_id, to_user_id, info from contact_request where from_user_id = #{fromUserId} and to_user_id = #{toUserId} and status = 0")
    ContactRequest getAddContact(String fromUserId, String toUserId);

    /**根据账号获取用户的联系人信息*/
    @Select("select * from contact where user_id = #{userId}")
    Contact getUserContactById(String userId);

    /**更新添加联系人请求的备注*/
    @Update("update contact_request set info = #{info},update_date = #{nowDate} where from_user_id = #{fromUserId} and to_user_id = #{toUserid}")
    void updateAddContact(String fromUserId, String toUserid, String info, String nowDate);

    /**获取黑名单列表*/
    @Select("select black_list from user_black_list where user_id = #{userId} and del_flag= 0")
    String getBlackList(String userId);

    /**更新黑名单列表*/
    @Update("update user_black_list set black_list = #{blackList} where user_id = #{userId} and del_flag = 0")
    void setBlackList(String userId, String blackList);
}