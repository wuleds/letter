package cn.wule.letter.channel.model;
//汉江师范学院 数计学院 吴乐创建于2024 4月 12 23:00

import lombok.Data;

@Data
public class ChannelInfo
{
    private String channelId;
    private String channelName;
    private String channelInfo;
    private String creatorId;
    private String channelPhoto;
    private String createDate;
    private int userCount;
}