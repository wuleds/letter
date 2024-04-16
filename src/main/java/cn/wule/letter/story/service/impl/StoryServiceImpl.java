package cn.wule.letter.story.service.impl;
//汉江师范学院 数计学院 吴乐创建于2024 4月 16 21:10

import cn.wule.letter.story.dao.StoryDao;
import cn.wule.letter.story.service.StoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class StoryServiceImpl implements StoryService {
    @Resource
    private StoryDao storyDao;


}