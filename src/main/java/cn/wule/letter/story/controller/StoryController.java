package cn.wule.letter.story.controller;
//汉江师范学院 数计学院 吴乐创建于2024 4月 16 21:05

import cn.wule.letter.model.user.User;
import cn.wule.letter.story.model.Comment;
import cn.wule.letter.story.model.Story;
import cn.wule.letter.story.service.StoryService;
import cn.wule.letter.util.JsonUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
        if(story.getText().isEmpty() && story.getImage().isEmpty() && story.getVideo().isEmpty()){
            return jsonUtil.createResponseModelJsonByString(code,msg,null);
        }
        if(story.getText().length() > 200){
            return jsonUtil.createResponseModelJsonByString(code,"文字内容过长",null);
        }
        storyService.createStory(myId,story.getText(),story.getImage(),story.getVideo());
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
    @PostMapping("/get/me")
    public String getStoryInfo()
    {
        String myId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        String data = storyService.getStoryListById(myId);
        return jsonUtil.createResponseModelJsonByString("200","获取成功",data);
    }

    /**获取好友动态*/
    @PostMapping("/get/{friendId}")
    public String getFriendStory(@PathVariable String friendId)
    {
        String myId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        if(friendId == null || friendId.isEmpty()){
            return jsonUtil.createResponseModelJsonByString("400","获取失败",null);
        }
        String data = storyService.getFriendStoryById(friendId,myId);
        return jsonUtil.createResponseModelJsonByString("200","获取成功",data);
    }

    /**获取故事评论*/
    @PostMapping("/comment/get/{storyId}")
    public String getStoryComment(@PathVariable String storyId)
    {
        if(storyId == null || storyId.isEmpty()){
            return jsonUtil.createResponseModelJsonByString("400","获取失败",null);
        }
        String data = storyService.getCommentListById(storyId);
        return jsonUtil.createResponseModelJsonByString("200","获取成功",data);
    }

    /**评论动态*/
    @PostMapping("/comment/add")
    public String  commentStory(@RequestBody Comment comment)
    {
        String myId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        if(comment == null){
            return jsonUtil.createResponseModelJsonByString("400","获取失败",null);
        }
        if(comment.getStoryId() == null || comment.getStoryId().isEmpty()){
            return jsonUtil.createResponseModelJsonByString("400","获取失败",null);
        }
        if(comment.getText() == null || comment.getText().isEmpty()){
            return jsonUtil.createResponseModelJsonByString("400","获取失败",null);
        }
        storyService.createComment(comment.getStoryId(),myId,comment.getText());
        return jsonUtil.createResponseModelJsonByString("200","评论成功",null);
    }

    /**喜爱动态*/
    @PostMapping("/like/{storyId}")
    public void likeStory(@PathVariable String storyId)
    {
        String myId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        if(storyId == null || storyId.isEmpty()){
            return;
        }
        storyService.likeStory(storyId,myId);
    }

    /**取消喜爱动态*/
    @PostMapping("/like/off/{storyId}")
    public void unlikeStory(@PathVariable String storyId)
    {
        if(storyId == null || storyId.isEmpty()){
            return;
        }
        storyService.cancelLikeStory(storyId,((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
    }
}