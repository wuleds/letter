package cn.wule.letter.group.model;
//汉江师范学院 数计学院 吴乐创建于2024 4月 16 23:34

import lombok.Data;

@Data
public class GroupRequest
{
    private String id;          //申请ID
    private String groupId;     //群组ID
    private String groupName;   //群组名
    private String userId;      //申请者ID
    private String info;        //申请信息
    private String status;      //申请状态
    private String createData; //申请时间
}