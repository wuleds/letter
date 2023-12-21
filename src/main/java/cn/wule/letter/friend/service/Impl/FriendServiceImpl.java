package cn.wule.letter.friend.service.Impl;
//汉江师范学院 数计学院 吴乐创建于2023/12/21 18:42

import cn.wule.letter.friend.dao.FriendDao;
import cn.wule.letter.friend.model.Friend;
import cn.wule.letter.friend.model.FriendRequest;
import cn.wule.letter.friend.model.FriendRequestHandle;
import cn.wule.letter.friend.service.FriendService;
import cn.wule.letter.util.NowDate;
import cn.wule.letter.util.UUIDUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;


/**
 * 好友模块业务层
 */
@Service
@Slf4j
public class FriendServiceImpl implements FriendService
{
    @Resource
    private FriendDao friendDao;
    @Resource
    private ObjectMapper om;

    /**添加好友请求*/
    @Override
    public void addAddFriend(String fromUserId, String toUserid, String info) {
        if(friendDao.getAddFriend(fromUserId,toUserid) == null){
            friendDao.addAddFriend(UUIDUtil.getUUID(),fromUserId,toUserid,info, NowDate.getNowDate());
        }else {
            friendDao.updateAddFriend(fromUserId,toUserid,info,NowDate.getNowDate());
        }
    }

    /** 更新用户的好友列表*/
    @Override
    public boolean addFriend(String userId, String friendId) {
        //获取用户好友列表
        //反序列为Set<String>格式,添加信息
        //序列化为json格式,存入数据库
        Friend friend = friendDao.getUserFriendById(userId);
        String json = friend.getFriendList();
        //获取用户好友列表的Set。
        Set<String> friendList = om.convertValue(json, om.getTypeFactory().constructCollectionType(Set.class, String.class));
        friendList.add(friendId);
        String friendListJson = json;
        try {
            friendListJson = om.writeValueAsString(friendList);
        } catch (JsonProcessingException e) {
            log.error("序列化用户好友列表失败");
            return false;
        }
        friendDao.setFriend(userId,friendListJson);
        return true;
    }

    /**删除好友*/
    @Override
    public boolean deleteFriend(String userId, String friendId) {
        //获取用户好友列表
        //反序列为Set<String>格式,删除信息
        //序列化为json格式,存入数据库
        Friend friend = friendDao.getUserFriendById(userId);
        String json = friend.getFriendList();
        //获取用户好友列表的Set。
        Set<String> friendList = om.convertValue(json, om.getTypeFactory().constructCollectionType(Set.class, String.class));
        //删除好友
        friendList.remove(friendId);

        String friendListJson = json;
        try {
            friendListJson = om.writeValueAsString(friendList);
        } catch (JsonProcessingException e) {
            log.error("序列化用户好友列表失败");
            return false;
        }
        friendDao.setFriend(userId,friendListJson);
        return true;
    }

    /**处理好友请求*/
    @Override
    public boolean handleAddFriend(FriendRequestHandle handle) {
        String fromUserId = handle.getFromUserId();
        String toUserId = handle.getToUserId();
        int status = handle.getStatus();
        FriendRequest friendRequest = friendDao.getAddFriend(fromUserId,toUserId);
        if(friendRequest != null){
            //更新好友请求状态,由被请求者处理
            friendDao.handleAddFriend(fromUserId,toUserId,status,NowDate.getNowDate());
            //如果同意,则添加好友
            if (status == 1){
                return addFriend(fromUserId, toUserId) && addFriend(toUserId, fromUserId);
            }else {
                return true;
            }
        }
        return false;
    }
}