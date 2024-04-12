package cn.wule.letter.group.service;

import cn.wule.letter.group.model.GroupInfo;

/**
 * 群组业务层接口
 * */
public interface GroupService
{
    /**创建群组*/
    void createGroup(String groupId,String groupName,String info,String creatorId,String groupPhoto);

    /**添加群组用户*/
    void addGroupUserList(String groupId,String userId,String position);

    /**删除群组成员*/
    void deleteGroupUser(String groupId,String userId);

    /**更新群组黑名单名单*/
    void blacklistGroupUser(String groupId,String blackList);

    /**添加管理员*/
    void addGroupAdmin(String groupId,String userId);

    /**取消管理员*/
    void cancelGroupAdmin(String groupId,String userId);

    /**删除群组所有成员*/
    void deleteGroupAllUserList(String groupId);

    /**获取群组用户数*/
    int getGroupUserCount(String groupId);

    /**获取群组信息*/
    GroupInfo getGroupInfo(String groupId);

    /**获取群组黑名单*/
    String getBlackList(String groupId);
}