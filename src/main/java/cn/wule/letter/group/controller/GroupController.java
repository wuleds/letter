package cn.wule.letter.group.controller;
//汉江师范学院 数计学院 吴乐创建于2024 4月 12 23:25

import cn.wule.letter.group.model.GroupInfo;
import cn.wule.letter.group.model.GroupRequest;
import cn.wule.letter.group.service.GroupService;
import cn.wule.letter.model.user.User;
import cn.wule.letter.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

/**
 * 群组控制层
 * */
@RestController
@RequestMapping("/group")
@Slf4j
public class GroupController {
    @Resource
    private GroupService groupService;
    @Resource
    private JsonUtil jsonUtil;

    /**
     * 创建群组
     */
    @PostMapping("/create")
    public String createGroup(@RequestBody GroupInfo groupInfo) {
        String code;
        String msg;
        String groupName = groupInfo.getGroupName();
        String info = groupInfo.getGroupInfo();
        String creatorId = groupInfo.getCreatorId();
        String groupPhoto = groupInfo.getGroupPhoto();
        String userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        if (groupName == null || info == null || groupPhoto == null || !userId.equals(creatorId)) {
            code = "400";
            msg = "创建群组参数错误";
        } else {
            if (groupService.createGroup(groupName, info, creatorId, groupPhoto)) {
                code = "200";
                msg = "创建群组成功";
            } else {
                code = "500";
                msg = "创建群组失败";
            }
        }
        return jsonUtil.createResponseModelJsonByString(code, msg, null);
    }

    /**
     * 申请加入群组
     */
    @PostMapping("/request")
    public String joinGroup(@RequestBody GroupInfo groupInfo) {
        String code;
        String msg;
        String groupId = groupInfo.getGroupId();
        String info = groupInfo.getGroupInfo();
        String userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        if (groupId == null) {
            code = "400";
            msg = "入群参数错误";
        } else {
            if (groupService.addGroupUserJoinRequest(groupId, userId, info)) {
                code = "200";
                msg = "已经发送申请";
            } else {
                code = "500";
                msg = "发送申请失败";
            }
        }
        return jsonUtil.createResponseModelJsonByString(code, msg, null);
    }

    /**
     * 获取群组的入群申请
     */
    @PostMapping("/request/get")
    public String getGroupRequest(@RequestBody GroupInfo groupInfo) {
        String code;
        String msg;
        String data = null;
        String groupId = groupInfo.getGroupId();
        String userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        if (groupId == null) {
            code = "400";
            msg = "获取入群申请参数错误";
        } else {
            data = groupService.getGroupUserJoinRequestList(groupId, userId);
            if (data != null) {
                code = "200";
                msg = "获取入群申请成功";
            } else {
                code = "500";
                msg = "获取入群申请失败";
            }
        }
        return jsonUtil.createResponseModelJsonByString(code, msg, data);
    }

    /**
     * 退出群组
     */
    @PostMapping("/quit")
    public String quitGroup(@RequestBody GroupInfo groupInfo) {
        String code;
        String msg;
        String groupId = groupInfo.getGroupId();
        String userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        if (groupService.deleteGroupUser(groupId, userId)) {
            code = "200";
            msg = "退出群组成功";
        } else {
            code = "500";
            msg = "退出群组失败";
        }
        return jsonUtil.createResponseModelJsonByString(code, msg, null);
    }

    /**
     * 解散群组
     */
    @PostMapping("/dismiss")
    public void dismissGroup() {
        log.info("解散群组");
    }

    /**
     * 获取群组信息
     */
    @PostMapping("/info")
    public String getGroupInfo(@RequestBody GroupInfo groupInfo) {
        String code;
        String msg;
        String data = null;
        String groupId = groupInfo.getGroupId();
        if (groupId == null) {
            code = "400";
            msg = "获取群组信息参数错误";
        } else {
            data = groupService.getGroupInfo(groupId);
            if (data != null) {
                code = "200";
                msg = "获取群组信息成功";
            } else {
                code = "500";
                msg = "没有群组消息";
            }
        }
        return jsonUtil.createResponseModelJsonByString(code, msg, data);
    }

    /**
     * 获取群组所有成员的列表
     */
    @PostMapping("/members")
    public String getGroupMembers(@RequestBody GroupInfo groupInfo) {
        String code;
        String msg;
        String data = null;
        String groupId = groupInfo.getGroupId();
        String userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        if (groupId == null) {
            code = "400";
            msg = "获取群组成员参数错误";
        } else {
            data = String.valueOf(groupService.getGroupUserList(groupId, userId));
            if (data != null) {
                code = "200";
                msg = "获取群组成员成功";
            } else {
                code = "500";
                msg = "获取群组成员失败";
            }
        }
        return jsonUtil.createResponseModelJsonByString(code, msg, data);
    }

    /**
     * 增加管理员
     */
    @PostMapping("/addAdmin")
    public String addAdmin(@RequestBody GroupRequest groupRequest) {
        String adminId = groupRequest.getUserId();
        String groupId = groupRequest.getGroupId();
        String creatorId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();

        String code;
        String msg;
        if (adminId == null || groupId == null) {
            code = "400";
            msg = "增加管理员参数错误";
        } else {
            if (groupService.addGroupAdmin(groupId, creatorId, adminId)) {
                code = "200";
                msg = "增加管理员成功";
            } else {
                code = "500";
                msg = "增加管理员失败";
            }
        }
        return jsonUtil.createResponseModelJsonByString(code, msg, null);
    }

    /**
     * 删除管理员
     */
    @PostMapping("/deleteAdmin")
    public String deleteAdmin(@RequestBody GroupRequest groupRequest) {
        String code;
        String msg;
        String adminId = groupRequest.getUserId();
        String groupId = groupRequest.getGroupId();
        String creatorId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();

        if (adminId == null || groupId == null) {
            code = "400";
            msg = "删除管理员参数错误";
        } else {
            if (groupService.cancelGroupAdmin(groupId, creatorId, adminId)) {
                code = "200";
                msg = "删除管理员成功";
            } else {
                code = "500";
                msg = "删除管理员失败";
            }
        }
        return jsonUtil.createResponseModelJsonByString(code, msg, null);
    }

    /**
     * 获取加入的群组
     */
    @RequestMapping("/list")
    public String getJoinGroups() {
        String code;
        String msg;
        String data = null;
        String userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        if (userId == null) {
            code = "400";
            msg = "获取加入的群组参数错误";
        } else {
            data = groupService.getUserGroupList(userId);
            if (data != null) {
                code = "200";
                msg = "获取加入的群组成功";
            } else {
                code = "500";
                msg = "获取加入的群组失败或用户没有加入群组";
            }
        }
        return jsonUtil.createResponseModelJsonByString(code, msg, data);
    }
}