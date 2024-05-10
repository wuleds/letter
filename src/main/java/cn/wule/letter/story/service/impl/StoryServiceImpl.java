package cn.wule.letter.story.service.impl;
//汉江师范学院 数计学院 吴乐创建于2024 4月 16 21:10

import cn.wule.letter.story.dao.StoryDao;
import cn.wule.letter.story.service.StoryService;
import cn.wule.letter.util.UUIDUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StoryServiceImpl implements StoryService {
    private static final Logger log = LoggerFactory.getLogger(StoryServiceImpl.class);
    @Resource
    private StoryDao storyDao;
    @Resource
    private ObjectMapper om;

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
    }
}