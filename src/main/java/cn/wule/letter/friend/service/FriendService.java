package cn.wule.letter.friend.service;

import cn.wule.letter.friend.model.FriendRequestHandle;

public interface FriendService
{
    void addAddFriend(String fromUserId, String toUserid, String info);

    boolean addFriend(String userId, String friendId);

    boolean deleteFriend(String userId, String friendId);

    boolean handleAddFriend(FriendRequestHandle handle);
}