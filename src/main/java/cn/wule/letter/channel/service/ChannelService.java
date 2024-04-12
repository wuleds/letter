package cn.wule.letter.channel.service;

import cn.wule.letter.channel.model.ChannelInfo;

/**
 * 频道业务层接口
 * */
public interface ChannelService
{
    /**创建频道*/
    void createChannel(String channelId,String channelName,String info,String creatorId,String channelPhoto);

    /**添加频道用户*/
    void addChannelUserList(String channelId,String userId,String position);

    /**删除频道成员*/
    void deleteChannelUser(String channelId,String userId);

    /**更新频道黑名单名单*/
    void blacklistChannelUser(String channelId,String blackList);

    /**添加管理员*/
    void addChannelAdmin(String channelId,String userId);

    /**取消管理员*/
    void cancelChannelAdmin(String channelId,String userId);

    /**删除频道所有成员*/
    void deleteChannelAllUserList(String channelId);

    /**获取频道用户数*/
    int getChannelUserCount(String channelId);

    /**获取频道信息*/
    ChannelInfo getChannelInfo(String channelId);

    /**获取频道黑名单*/
    String getBlackList(String channelId);
}