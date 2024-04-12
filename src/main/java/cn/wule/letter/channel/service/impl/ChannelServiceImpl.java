package cn.wule.letter.channel.service.impl;
//汉江师范学院 数计学院 吴乐创建于2024 4月 12 23:24

import cn.wule.letter.channel.dao.ChannelDao;
import cn.wule.letter.channel.model.ChannelInfo;
import cn.wule.letter.channel.service.ChannelService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChannelServiceImpl implements ChannelService {
    @Resource
    private ChannelDao channelDao;

    @Override
    public void createChannel(String channelId, String channelName, String info, String creatorId, String channelPhoto) {

    }

    @Override
    public void addChannelUserList(String channelId, String userId, String position) {

    }

    @Override
    public void deleteChannelUser(String channelId, String userId) {

    }

    @Override
    public void blacklistChannelUser(String channelId, String blackList) {

    }

    @Override
    public void addChannelAdmin(String channelId, String userId) {

    }

    @Override
    public void cancelChannelAdmin(String channelId, String userId) {

    }

    @Override
    public void deleteChannelAllUserList(String channelId) {

    }

    @Override
    public int getChannelUserCount(String channelId) {
        return 0;
    }

    @Override
    public ChannelInfo getChannelInfo(String channelId) {
        return null;
    }

    @Override
    public String getBlackList(String channelId) {
        return "";
    }
}