package cn.wule.letter.story.controller;
//汉江师范学院 数计学院 吴乐创建于2024 4月 16 21:05

import cn.wule.letter.model.user.User;
import cn.wule.letter.story.model.Story;
import cn.wule.letter.story.service.StoryService;
import cn.wule.letter.util.JsonUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/story")
@Slf4j
public class StoryController
{
    @Resource
    private StoryService storyService;
    @Resource
    private JsonUtil jsonUtil;

    /**发布动态*/
    @PostMapping("/create")
    public String createStory(@RequestBody Story story)
    {
        String myId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        String code = "400";
        String msg = "发布失败";
        if(story == null){
            return jsonUtil.createResponseModelJsonByString(code,msg,null);
        }
        if(story.getSenderId() == null || story.getSenderId().isEmpty()){
            return jsonUtil.createResponseModelJsonByString(code,msg,null);
        }
        if(story.getText() == null && story.getImage() == null && story.getVideo() == null){
            return jsonUtil.createResponseModelJsonByString(code,msg,null);
        }
        if(story.getText() != null && story.getText().length() > 200){
            return jsonUtil.createResponseModelJsonByString(code,"文字内容过长",null);
        }
        storyService.createStory(story.getSenderId(),story.getText(),story.getImage(),story.getVideo());
        return jsonUtil.createResponseModelJsonByString("200","发布成功",null);
    }

    /**删除动态*/
    @PostMapping("/delete")
    public String deleteStory(@RequestBody Story story)
    {
        String myId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        if(story == null){
            return jsonUtil.createResponseModelJsonByString("400","删除失败",null);
        }
        if(story.getId() == null || story.getId().isEmpty()){
            return jsonUtil.createResponseModelJsonByString("400","删除失败",null);
        }
        storyService.deleteStory(story.getId(),myId);
        return jsonUtil.createResponseModelJsonByString("200","操作成功",null);
    }

    /**获取动态*/
    @PostMapping("/get")
    public String getStoryInfo()
    {
        String myId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        String data = storyService.getStoryListById(myId);
        return jsonUtil.createResponseModelJsonByString("200","获取成功",data);
    }

    /**获取用户动态列表*/
    @PostMapping("/list")
    public void getStoryList()
    {
        log.info("获取故事列表");
    }


    /**获取故事评论*/
    @RequestMapping("/comment/get")
    public String getStoryComment(@RequestBody Story story)
    {
        log.info("获取故事评论");
        return null;
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