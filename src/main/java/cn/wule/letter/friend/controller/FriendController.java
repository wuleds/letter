package cn.wule.letter.friend.controller;
//汉江师范学院 数计学院 吴乐创建于2023/12/21 18:03

import cn.wule.letter.friend.model.FriendRequest;
import cn.wule.letter.friend.model.FriendRequestHandle;
import cn.wule.letter.friend.service.FriendService;
import cn.wule.letter.model.user.User;
import cn.wule.letter.user.service.UserService;
import cn.wule.letter.util.JsonUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;

/**
 * 好友关系处理
 */
@RestController
@Slf4j
@RequestMapping("/friend")
public class FriendController
{
    @Resource
    private FriendService friendService;
    @Resource
    private UserService userService;
    @Resource
    private JsonUtil jsonUtil;

    /**发送好友请求*/
    @PostMapping("/add")
    public String friendRequest(@RequestBody FriendRequest friendRequest)
    {
        String code = "500";
        String msg = "发送好友请求失败";
        String fromUserId = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        if(friendRequest.getToUserId() == null || friendRequest.getToUserId().isEmpty()){
            code = "400";
            msg = "参数为空";
        }else if(friendRequest.getFromUserId().equals(friendRequest.getToUserId())) {
            code = "400";
            msg = "不能添加自己为好友";
        }else if(userService.getUserById(friendRequest.getToUserId()) == null || userService.getUserById(friendRequest.getFromUserId()) == null){
            code = "400";
            msg = "用户不存在";
        }else {
            friendService.addAddFriend(fromUserId,friendRequest.getToUserId(),friendRequest.getInfo());
            code = "200";
            msg = "发送好友请求成功";
        }
        return jsonUtil.createResponseModelJsonByString(code,msg,null);
    }

    /**处理好友请求*/
    @PostMapping("/handle")
    public String handleFriendRequest(@RequestBody FriendRequestHandle handle)
    {
        String code = "500";
        String msg = "处理好友请求失败";
        String toUserId = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        handle.setToUserId(toUserId);
        if(handle.getFromUserId() == null || handle.getFromUserId().isEmpty()) {
            code = "400";
            msg = "参数为空";
        }else if (handle.getFromUserId().equals(handle.getToUserId())) {
            code = "400";
            msg = "不能添加自己为好友";
        } else if(userService.getUserById(handle.getFromUserId()) == null) {
            code = "400";
            msg = "用户不存在";
        }else {
            friendService.handleAddFriend(handle);
            code = "200";
            msg = "处理好友请求成功";
        }
        return jsonUtil.createResponseModelJsonByString(code,msg,null);
    }

    /**删除好友*/
    @PostMapping("/delete")
    public String deleteFriend(@RequestBody FriendRequest friendRequest)
    {
        String code = "500";
        String msg = "删除好友失败";
        String fromUserId = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        if(friendRequest.getToUserId() == null || friendRequest.getToUserId().isEmpty()) {
            code = "400";
            msg = "参数为空";
        }else if(fromUserId.equals(friendRequest.getToUserId())) {
            code = "400";
            msg = "不能删除自己";
        }else if(userService.getUserById(friendRequest.getToUserId()) == null) {
            code = "400";
            msg = "用户不存在";
        }else {
            friendService.deleteFriend(fromUserId,friendRequest.getToUserId());
            code = "200";
            msg = "删除好友成功";
        }
        return jsonUtil.createResponseModelJsonByString(code,msg,null);
    }
}