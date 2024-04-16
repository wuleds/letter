package cn.wule.letter.group.service;

import cn.wule.letter.model.user.UserInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 群组业务层接口
 * */
public interface GroupService
{
    @Transactional
    boolean createGroup(String groupName, String info, String creatorId, String groupPhoto);

    /**
     * 添加入群申请
     */
    boolean addGroupUserJoinRequest(String groupId, String userId, String info);

    /**
     * 处理入群申请
     */
    boolean handleGroupUserJoinRequest(String requestId, String groupId, String handlerId, String toId, String status);

    /**
     * 获取入群申请列表
     */
    String  getGroupUserJoinRequestList(String groupId, String userId);

    /**删除群组成员*/
    boolean deleteGroupUser(String groupId, String userId);

    /**
     * 更新群组黑名单名单
     **/
    boolean blacklistGroupUser(String groupId, String blackList);

    /**
     * 添加管理员
     **/
    boolean addGroupAdmin(String groupId, String creatorId,String userId);

    /**
     * 取消管理员
     */
    boolean cancelGroupAdmin(String groupId,String creatorId, String userId);

    /**获取群组用户数*/
    int getGroupUserCount(String groupId);

    /**
     * 获取群组信息
     */
    String getGroupInfo(String groupId);

    /**
     * 获取群组黑名单
     */
    List<String> getBlackList(String groupId);

    /**获取群组所有成员列表 */
    List<UserInfo> getGroupUserList(String groupId,String userId);

    /**
     * 获取用户加入的所有群组列表
     */
    String getUserGroupList(String userId);
}