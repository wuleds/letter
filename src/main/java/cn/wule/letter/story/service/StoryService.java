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

}