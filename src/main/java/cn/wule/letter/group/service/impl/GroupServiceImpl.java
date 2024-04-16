package cn.wule.letter.group.service.impl;
//汉江师范学院 数计学院 吴乐创建于2024 4月 12 23:22

import cn.wule.letter.group.dao.GroupDao;
import cn.wule.letter.group.model.GroupInfo;
import cn.wule.letter.group.service.GroupService;
import cn.wule.letter.model.user.UserInfo;
import cn.wule.letter.user.dao.UserInfoDao;
import cn.wule.letter.util.UUIDUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 群组业务层实现类
 * */
@Service
@Slf4j
public class GroupServiceImpl implements GroupService {
    @Resource
    private GroupDao groupDao;
    @Resource
    private ObjectMapper om;
    @Resource
    private UserInfoDao userInfoDao;

    @Override
    @Transactional
    public boolean createGroup(String groupName, String info, String creatorId, String groupPhoto) {
        try {
            //获取groupId
            String groupId = UUIDUtil.getUUID();
            //添加新群组信息
            groupDao.createGroup(groupId, groupName, info, creatorId, groupPhoto);
            //设置id类型
            groupDao.setGroupIdType(groupId);
            //创建群组聊天信息表
            groupDao.createGroupChat(groupId);
            //添加群组创建者
            groupDao.addGroupUser(groupId, creatorId, "creator");
            //获取用户加入的群组列表的Set
            Set<String> groupSet = updateUserJoinGroup(creatorId);
            //更新用户加入的群组
            groupDao.updateUserGroupList(creatorId, om.writeValueAsString(groupSet));
            return true;
        } catch (Exception e) {
            log.error("创建群组失败", e);
        }
        return false;
    }

    /**
     * 添加入群申请
     *
     * @return bool
     */
    @Override
    public boolean addGroupUserJoinRequest(String groupId, String userId, String info) {
        try {
            String requestId = UUIDUtil.getUUID();
            String blackListJson = groupDao.getBlackList(groupId);
            //黑名单中没有该用户并且该用户不在群组中
            if (!blackListJson.contains(userId) && groupDao.getUserPositionInGroup(groupId, userId) == null) {
                //添加入群申请
                groupDao.applyGroup(requestId, groupId, userId, info);
            }
            return true;
        } catch (Exception e) {
            log.error("添加群组成员失败", e);
        }
        return false;
    }

    /**
     * 处理入群申请
     */
    @Override
    public boolean handleGroupUserJoinRequest(String requestId, String groupId, String handlerId, String toId, String status) {
        try {
            //获取该用户在该群组的身份
            String position = groupDao.getUserPositionInGroup(groupId, handlerId);
            if (Objects.equals(position, "admin") && Objects.equals(position, "creator")) {
                //处理入群申请
                groupDao.handleGroupRequest(requestId, groupId, status);
                if (Objects.equals(status, "1")) {
                    //添加群组成员
                    groupDao.addGroupUser(groupId, toId, "member");
                }
            }
            return true;
        } catch (Exception e) {
            log.error("处理入群申请失败", e);
        }
        return false;
    }

    /**
     * 获取入群申请列表
     */
    @Override
    public String getGroupUserJoinRequestList(String groupId, String userId) {
        //获取该用户在该群组的身份
        String position = groupDao.getUserPositionInGroup(groupId, userId);
        if (Objects.equals(position, "admin") && Objects.equals(position, "creator")) {
            //获取入群申请
            try {
                return om.writeValueAsString(groupDao.getGroupRequest(groupId));
            } catch (JsonProcessingException e) {
                log.error("获取入群申请列表失败", e);
            }
        }
        return null;
    }

    /**
     * 删除群组成员
     */
    @Override
    public boolean deleteGroupUser(String groupId, String userId) {
        try {
            groupDao.deleteGroupUser(groupId, userId);
            //获取用户加入的群组列表的Set
            Set<String> groupSet = updateUserJoinGroup(userId);
            //删除该群组
            groupSet.remove(groupId);
            //序列化为json格式
            //更新用户加入的群组
            groupDao.updateUserGroupList(userId, om.writeValueAsString(groupSet));

            return true;
        } catch (Exception e) {
            log.error("删除群组成员失败", e);
        }
        return false;
    }

    private Set<String> updateUserJoinGroup(String userId) throws JsonProcessingException {
        String groupListJson = groupDao.getUserGroupList(userId);
        Set<String> groupSet;
        if (groupListJson == null) {
            groupSet = new HashSet<String>();
        } else {
            //转为set
            groupSet = om.readValue(groupListJson, new TypeReference<Set<String>>() {
            });
        }
        return groupSet;
    }

    /**
     * 添加黑名单
     *
     * @return bool
     */
    @Override
    @Transactional
    public boolean blacklistGroupUser(String groupId, String userId) {
        String blackListJson = groupDao.getBlackList(groupId);
        //转为set
        Set<String> blackList;
        try {
            try {
                //获取群组黑名单
                blackList = om.readValue(blackListJson, new TypeReference<Set<String>>() {
                });
            } catch (Exception e) {
                //黑名单对象可能为null
                blackList = new HashSet<String>();
            }
            blackList.add(userId);

            try {
                //序列化为json格式
                blackListJson = om.writeValueAsString(blackList);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            //更新黑名单
            groupDao.blacklistGroupUser(groupId, blackListJson);
            //删除该群组成员
            groupDao.deleteGroupUser(groupId, userId);
            return true;
        } catch (Exception e) {
            log.error("添加黑名单失败", e);
        }
        return false;
    }

    @Override
    public boolean addGroupAdmin(String groupId, String creatorId, String userId) {
        //获取该用户在该群组的身份
        String position = groupDao.getUserPositionInGroup(groupId, creatorId);
        if (Objects.equals(position, "creator")) {
            try {
                groupDao.addGroupAdmin(groupId, userId);
                return true;
            } catch (Exception e) {
                log.error("添加管理员失败", e);
            }
        }
        return false;
    }

    /**
     * 取消管理员
     */
    @Override
    public boolean cancelGroupAdmin(String groupId, String creatorId, String adminId) {
        //获取该用户在该群组的身份
        String position = groupDao.getUserPositionInGroup(groupId, creatorId);
        if (Objects.equals(position, "creator")) {
            try {
                groupDao.cancelGroupAdmin(groupId, adminId);
                return true;
            } catch (Exception e) {
                log.error("取消管理员失败", e);
            }
        }
        return false;
    }

    /**
     * 获取群组成员数量
     */
    @Override
    public int getGroupUserCount(String groupId) {
        try {
            return groupDao.getGroupUserCount(groupId);
        } catch (Exception e) {
            log.error("获取群组成员数量失败", e);
        }
        return -1;
    }

    /**
     * 获取群组信息
     */
    @Override
    public String getGroupInfo(String groupId) {
        try {
            return om.writeValueAsString(groupDao.getGroupInfo(groupId));
        } catch (Exception e) {
            log.error("获取群组信息失败", e);
        }
        return null;
    }

    /**
     * 获取群组黑名单
     */
    @Override
    public List<String> getBlackList(String groupId) {
        try {
            //获取黑名单json
            String blackListJson = groupDao.getBlackList(groupId);
            //转为Set
            Set<String> blackSet = om.readValue(blackListJson, new TypeReference<Set<String>>() {
            });
            //转为List
            return new ArrayList<String>(blackSet);
        } catch (JsonProcessingException e) {
            log.error("获取黑名单失败", e);
        }
        return null;
    }

    /**
     * 获取群组所有成员列表
     */
    @Override
    public List<UserInfo> getGroupUserList(String groupId, String userId) {
        try {
            //获取该用户在该群组的身份
            String position = groupDao.getUserPositionInGroup(groupId, userId);
            if (Objects.equals(position, "admin") || Objects.equals(position, "creator")) {
                //获取群组成员
                List<String> userIds = groupDao.getGroupUser(groupId);
                List<UserInfo> userInfos = new ArrayList<UserInfo>();
                for (String id : userIds) {
                    //获取用户信息
                    UserInfo userInfo = userInfoDao.getALitterUserInfoById(id);
                    userInfos.add(userInfo);
                }
                return userInfos;
            }
        } catch (Exception e) {
            log.error("获取群组成员列表失败", e);
        }
        return null;
    }

    @Override
    public String getUserGroupList(String userId) {
        try {
            //获取用户加入的群组列表的json
            String groupListJson = groupDao.getUserGroupList(userId);
            //转为Set
            if (groupListJson == null) {
                log.info("用户加入的群组列表为空");
                return null;
            }
            Set<String> groupSet = om.readValue(groupListJson, new TypeReference<Set<String>>() {});
            List<GroupInfo> groupInfos = new ArrayList<GroupInfo>();
            for (String groupId : groupSet) {
                //获取群组信息
                GroupInfo groupInfo = groupDao.getGroupInfo(groupId);
                groupInfo.setUserCount(groupDao.getGroupUserCount(groupId));
                groupInfos.add(groupInfo);
            }
            return om.writeValueAsString(groupInfos);
        } catch (JsonProcessingException e) {
            log.error("获取用户加入的群组列表失败", e);
        }
        return null;
    }
}