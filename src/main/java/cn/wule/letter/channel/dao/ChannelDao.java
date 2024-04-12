package cn.wule.letter.channel.dao;

import cn.wule.letter.channel.model.ChannelInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 频道数据访问层
 * */
@Mapper
public interface ChannelDao
{
    /**添加频道*/
    @Insert("insert into channel(channel_id, channel_name, channel_info, creator_id, channel_photo, create_date, del_flag) " +
            "value (#{channelId}, #{channelName}, #{info}, #{creatorId}, #{channelPhoto}, now(),0)")
    void createChannel(String channelId,String channelName,String info,String creatorId,String channelPhoto);

    /**设置id类型*/
    @Insert("insert into chat_id_type(chat_id, type, del_flag) value (#{channelId}, 'channel', 0)")
    void setChannelIdType(String channelId);

    /**用户关注频道*/
    @Insert("insert into channel_user_list(channel_id, user_id, del_flag) value (#{channelId}, #{userId}, #{position}, 0)")
    void addChannelUserList(String channelId,String userId,String position);

    /**获取频道关注的个数*/
    @Select("select count(user_id) from channel_user_list where channel_id = #{channelId} and del_flag = 0")
    int getChannelUserCount(String channelId);

    /**取消关注*/
    @Update("update channel_user_list set del_flag = 1 where channel_id = #{channelId} and user_id = #{userId}")
    void cancelChannelUserList(String channelId,String userId);

    /**获取频道信息*/
    @Select("select channel_id, channel_name, channel_info, creator_id, channel_photo, create_date from channel where channel_id = #{channelId} and del_flag = 0")
    ChannelInfo getChannelInfo(String channelId);

    /**获取用户关注的频道列表*/
    @Select("select channel_list from user_join_channel where user_id = #{userId} and del_flag = 0")
    String  getChannelList(String userId);

    /**更新用户关注的频道列表*/
    @Update("update user_join_channel set channel_list = #{channelList} where user_id = #{userId} and del_flag = 0")
    void updateChannelList(String userId,String channelList);
}