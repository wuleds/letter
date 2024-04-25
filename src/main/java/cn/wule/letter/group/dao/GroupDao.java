package cn.wule.letter.group.dao;

import cn.wule.letter.group.model.GroupInfo;
import cn.wule.letter.group.model.GroupRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 群组数据访问层
 * */
@Mapper
public interface GroupDao
{
    /**添加群组信息*/
    @Insert("insert into chat_group(group_id, group_name, group_info, creator_id, manager_id_list, group_photo, create_date, del_flag) " +
            "value (#{groupId}, #{groupName}, #{info}, #{creatorId},null, #{groupPhoto},  now(),0)")
    void createGroup(String groupId,String groupName,String info,String creatorId,String groupPhoto);

    /**设置id类型*/
    @Insert("insert into chat_id_type(chat_id, type, del_flag) value (#{groupId}, 'group', 0)")
    void setGroupIdType(String groupId);

    /**创建群组聊天信息表*/
    void createGroupChat(@Param("groupId") String groupId);

    /**添加群组用户*/
    @Insert("insert into group_user_list(group_id, user_id, del_flag, user_position, date) value (#{groupId}, #{userId},0,#{position},now())")
    void addGroupUser(String groupId,String userId,String position);

    /**删除群组成员*/
    @Update("update group_user_list set del_flag = 1 where group_id = #{groupId} and user_id = #{userId} and del_flag = 0")
    void deleteGroupUser(String groupId,String userId);

    /**更新群组黑名单名单*/
    @Update("update black_list set black_list = #{blackList} where id = #{groupId} and del_flag = 0")
    void blacklistGroupUser(String groupId,String blackList);

    /**添加管理员*/
    @Update("update group_user_list set user_position = 'admin' where group_id = #{groupId} and user_id = #{userId}")
    void addGroupAdmin(String groupId,String userId);

    /**取消管理员*/
    @Update("update group_user_list set user_position = 'member' where group_id = #{groupId} and user_id = #{userId} and del_flag = 0")
    void cancelGroupAdmin(String groupId,String userId);

    /**获取群组用户数*/
    @Select("select count(user_id) from group_user_list where group_id = #{groupId} and del_flag = 0")
    int getGroupUserCount(String groupId);

    /**获取群组信息*/
    @Select("select group_id, group_name, group_info, creator_id, manager_id_list, group_photo, create_date " +
            "from chat_group " +
            "where (group_id = #{groupIdOrName} or group_name = #{groupIdOrName})" +
            "and del_flag = 0")
    List<GroupInfo> getGroupInfo(String groupIdOrName);

    /**根据群组id获取群组信息*/
    @Select("select * " +
            "from chat_group " +
            "where group_id = #{groupId} and del_flag = 0")
    GroupInfo  getGroupInfoById(String groupId);

    /**获取群组黑名单*/
    @Select("select black_list from black_list where id = #{groupId} and del_flag = 0")
    String getBlackList(String groupId);

    /**获取群组管理员*/
    @Select("select user_id from group_user_list where group_id = #{groupId} and user_position = 'admin' and del_flag = 0")
    String getGroupAdmin(String groupId);

    /**获取群组成员*/
    @Select("select user_id from group_user_list where group_id = #{groupId} and del_flag = 0")
    List<String> getGroupUser(String groupId);

    /**申请加入群组*/
    @Insert("insert into group_request(id,group_id, user_id, status,info) value (#{id},#{groupId}, #{userId},0, #{info})")
    void applyGroup(String id,String groupId,String userId,String info);

    /**获取群组申请*/
    @Select("select id,group_id, user_id,info,created_date from group_request where group_id = #{groupId} and status = 0")
    List<GroupRequest> getGroupRequest(String groupId);

    /**处理群组申请*/
    @Update("update group_request set status = #{status} where id = #{id} and group_id = #{groupId} and status = 0")
    void handleGroupRequest(String id,String groupId,String status);

    /**获取用户在群组里的身份*/
    @Select("select user_position from group_user_list where group_id = #{groupId} and user_id = #{userId} and del_flag = 0")
    String getUserPositionInGroup(String groupId,String userId);

    /**更新用户加入的群组的列表*/
    @Update("update user_join_group set group_list = #{groupList} where user_id = #{userId} and del_flag = 0")
    void updateUserGroupList(String userId,String groupList);

    /**获取用户加入的群组列表*/
    @Select("select group_list from user_join_group where user_id = #{userId} and del_flag = 0")
    String getUserGroupList(String userId);

    @Insert("insert into black_list(id,black_list,del_flag) value (#{groupId},'[]',0)")
    void createBlackList(String groupId);

    /**根据群组id获取群组名字*/
    @Select("select group_name from chat_group where group_id = #{groupId} and del_flag = 0")
    String getGroupNameById(String groupId);

    /**获取用户头像地址*/
    @Select("select user_photo from user_info where user_id = #{userId} and del_flag = 1;")
    String getUserPhoto(String userId);

    /**获取群组头像*/
    @Select("select group_photo from chat_group where group_id = #{groupId} and del_flag = 0;")
    String getGroupPhoto(String groupId);

    /**获取频道头像*/
    @Select("select channel_photo from channel where channel_id = #{channelId} and del_flag = 0;")
    String getChannelPhoto(String channelId);

    /**获取群组名*/
    @Select("select group_name from chat_group where group_id = #{groupId} and del_flag = 0;")
    String getGroupName(String groupId);

    /**获取频道名*/
    @Select("select channel_name from channel where channel_id = #{channelId} and del_flag = 0;")
    String getChannelName(String channelId);
}