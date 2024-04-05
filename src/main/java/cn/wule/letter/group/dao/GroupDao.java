package cn.wule.letter.group.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 群组数据访问层
 * */
@Mapper
public interface GroupDao
{
    /**添加群组信息*/
    void createGroup(String groupName,String groupDescription,String groupOwnerId);

    /**设置id类型*/
    @Insert("insert into chat_id_type(chat_id, type, del_flag) value (#{groupId}, 'group', 0)")
    void setGroupIdType(String groupId);
}