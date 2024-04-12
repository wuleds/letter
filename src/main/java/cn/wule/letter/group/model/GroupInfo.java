package cn.wule.letter.group.model;
//汉江师范学院 数计学院 吴乐创建于2024 4月 12 23:02

import lombok.Data;

@Data
public class GroupInfo
{
    private String groupId; //群组id
    private String groupName; //群组名
    private String groupInfo; //群组信息
    private String creatorId; //创建者id
    private String managerIdList; //管理员id列表
    private String groupPhoto; //群组头像
    private String createDate; //创建时间
    private int userCount; //用户数
}