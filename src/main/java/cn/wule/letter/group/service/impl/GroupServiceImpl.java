package cn.wule.letter.group.service.impl;
//汉江师范学院 数计学院 吴乐创建于2024 4月 12 23:22

import cn.wule.letter.group.dao.GroupDao;
import cn.wule.letter.group.model.GroupInfo;
import cn.wule.letter.group.service.GroupService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 群组业务层实现类
 * */
@Service
@Slf4j
public class GroupServiceImpl implements GroupService {
    @Resource
    private GroupDao groupDao;

    @Override
    public void createGroup(String groupId, String groupName, String info, String creatorId, String groupPhoto) {

    }

    @Override
    public void addGroupUserList(String groupId, String userId, String position) {

    }

    @Override
    public void deleteGroupUser(String groupId, String userId) {

    }

    @Override
    public void blacklistGroupUser(String groupId, String blackList) {

    }

    @Override
    public void addGroupAdmin(String groupId, String userId) {

    }

    @Override
    public void cancelGroupAdmin(String groupId, String userId) {

    }

    @Override
    public void deleteGroupAllUserList(String groupId) {

    }

    @Override
    public int getGroupUserCount(String groupId) {
        return 0;
    }

    @Override
    public GroupInfo getGroupInfo(String groupId) {
        return null;
    }

    @Override
    public String getBlackList(String groupId) {
        return "";
    }
}