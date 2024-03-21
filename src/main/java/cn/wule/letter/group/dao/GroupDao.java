package cn.wule.letter.group.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * 群组数据访问层
 * */
@Mapper
public interface GroupDao
{
    void createGroup(String groupName,String groupDescription,String groupOwnerId);
}