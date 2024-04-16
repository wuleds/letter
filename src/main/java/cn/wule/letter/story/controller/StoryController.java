package cn.wule.letter.story.controller;
//汉江师范学院 数计学院 吴乐创建于2024 4月 16 21:05

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/story")
@Slf4j
public class StoryController
{
    /**发布动态*/
    @RequestMapping("/create")
    public void createStory()
    {
        log.info("创建故事");
    }

    /**删除动态*/
    @RequestMapping("/delete")
    public void deleteStory()
    {
        log.info("删除故事");
    }

    /**获取动态*/
    @RequestMapping("/info")
    public void getStoryInfo()
    {
        log.info("获取故事信息");
    }

    /**获取用户动态列表*/
    @RequestMapping("/list")
    public void getStoryList()
    {
        log.info("获取故事列表");
    }


    /**获取故事评论*/
    @RequestMapping("/comment/get")
    public void getStoryComment()
    {
        log.info("获取故事评论");
    }

    /**评论动态*/
    @RequestMapping("/comment/add")
    public void commentStory()
    {
        log.info("评论故事");
    }

    /**删除评论*/
    @RequestMapping("/comment/delete")
    public void deleteComment()
    {
        log.info("删除评论");
    }

    /**喜爱动态*/
    @RequestMapping("/like")
    public void likeStory()
    {
        log.info("点赞故事");
    }

    /**取消喜爱动态*/
    @RequestMapping("/unlike")
    public void unlikeStory()
    {
        log.info("取消点赞故事");
    }
}