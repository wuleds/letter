package cn.wule.letter.story.service.impl;
//汉江师范学院 数计学院 吴乐创建于2024 4月 16 21:10

import cn.wule.letter.contact.dao.ContactDao;
import cn.wule.letter.story.dao.StoryDao;
import cn.wule.letter.story.model.Story;
import cn.wule.letter.story.service.StoryService;
import cn.wule.letter.util.UUIDUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StoryServiceImpl implements StoryService {
    private static final Logger log = LoggerFactory.getLogger(StoryServiceImpl.class);
    @Resource
    private StoryDao storyDao;
    @Resource
    private ObjectMapper om;
    @Resource
    private ContactDao contactDao;

    @Override
    public void createStory(String senderId, String text, String image, String video) {
        storyDao.createStory(UUIDUtil.getUUID(), senderId, text, image, video);
    }

    @Override
    public void deleteStory(String id,String userId) {
        storyDao.deleteStory(id,userId);
    }

    @Override
    public String getStoryListById(String userId) {
        try {
            return om.writeValueAsString(storyDao.getStoryListById(userId));
        } catch (JsonProcessingException e) {
            log.error("获取动态列表失败",e);
        }
        return null;
    }

    @Override
    public String getFriendStoryById(String friendId,String userId){
        //确定好友关系
        if (contactDao.getContactById(friendId).contains(userId)) {
            try {
                return om.writeValueAsString(storyDao.getStoryListById(friendId));
            } catch (JsonProcessingException e) {
                log.error("获取好友动态列表失败",e);
            }
        }
        return null;
    }

    /**
     * 获取动态评论
     * */
    @Override
    public String getCommentListById(String storyId) {
        try {
            return om.writeValueAsString(storyDao.getCommentListById(storyId));
        } catch (JsonProcessingException e) {
            log.error("获取评论列表失败",e);
        }
        return null;
    }

    /**
     * 发表评论
     * */
    @Override
    public void createComment(String storyId, String senderId, String text) {
        storyDao.createComment(UUIDUtil.getUUID(),storyId,senderId,text);
    }

    @Override
    public void likeStory(String storyId, String userId) {
        storyDao.likeStory(storyId);
        String likerJson = storyDao.getLiker(storyId);
        Set<String> liker = null;
        try {
            liker = om.readValue(likerJson, new TypeReference<HashSet<String>>() {});
        } catch (Exception e) {
            liker = new HashSet<>();
        }
        liker.add(userId);
        try {
            storyDao.updateLiker(storyId,om.writeValueAsString(liker));
        } catch (JsonProcessingException e) {
            log.error("更新点赞者失败", e);
        }
    }

    @Override
    public void cancelLikeStory(String storyId, String userId) {
        storyDao.unlikeStory(storyId);
        String likerJson = storyDao.getLiker(storyId);
        Set<String> liker;
        try {
            liker = om.readValue(likerJson, new TypeReference<HashSet<String>>() {});
        } catch (JsonProcessingException e) {
            liker = new HashSet<>();
        }
        liker.remove(userId);
        try {
            storyDao.updateLiker(storyId,om.writeValueAsString(liker));
        } catch (JsonProcessingException e) {
            log.error("更新点赞者失败", e);
        }

    }


}