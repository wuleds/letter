package cn.wule.letter.story.service;

import java.util.List;

public interface StoryService
{
    /**
     * 创建动态
     */
    void createStory(String senderId, String text, String image, String video);

    /**
     * 删除动态
     */
    void deleteStory(String id,String userId);

    /**
     * 获取动态列表
     */
    String getStoryListById(String userId);

    /**
     * 获取好友动态列表
     */
    String getFriendStoryById(String friendId,String userId);

    /**
     * 获取动态评论
     */
    String getCommentListById(String storyId);

    /**
     * 发表评论
     */
    void createComment(String storyId,String senderId,String text);

    /**
     * 喜欢动态*/
    void likeStory(String storyId,String userId);

    /**
     * 取消喜欢动态
     */
    void cancelLikeStory(String storyId,String userId);
}