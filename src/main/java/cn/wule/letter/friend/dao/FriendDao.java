package cn.wule.letter.friend.dao;

import cn.wule.letter.friend.model.Friend;
import cn.wule.letter.friend.model.FriendRequest;
import org.apache.ibatis.annotations.*;

/**
 * 好友模块Dao层
 */
@Mapper
public interface FriendDao
{
    /**更新用户的好友列表*/
    @Update("update friend set friend_list = #{friendListJson} where user_id = #{userId}")
    void setFriend(String userId,String friendListJson);

    /** 添加好友请求*/
    @Insert("insert into add_friend (id,from_user_id,to_user_id,info,create_date,update_date) values (#{id},#{fromUserId},#{toUserId},#{info},#{createDate},#{createDate})")
    void addAddFriend(String id,String fromUserId,String toUserId,String info,String createDate);

    /**处理好友请求*/
    @Update("update add_friend set status = #{status},update_date = #{updateDate} where from_user_id = #{fromUserId} and to_user_id = #{toUserId}")
    void handleAddFriend(String fromUserId,String toUserId,int status,String updateDate);

    /**更新好友请求*/
    @Update("update add_friend set info = #{info},update_date = #{updateDate} where from_user_id = #{fromUserId} and to_user_id = #{toUserId} and status = 0")
    void updateAddFriend(String fromUserId,String toUserId,String info,String updateDate);

    /**获取好友请求*/
    @Select("select from_user_id, to_user_id, info from add_friend where from_user_id = #{fromUserId} and to_user_id = #{toUserId} and status = 0")
    FriendRequest getAddFriend(String fromUserId, String toUserId);

    /**根据账号获取用户的好友信息*/
    @Select("select * from friend where user_id = #{userId}")
    Friend getUserFriendById(String userId);
}