package cn.wule.letter.story.dao;

import cn.wule.letter.story.model.Comment;
import cn.wule.letter.story.model.Story;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StoryDao
{
    /**
     * 创建动态
     */
    @Insert("insert into story(id, senderId, text, image, video, liker, comment, create_date, del_flag) values(#{id}, #{senderId}, #{text}, #{image}, #{video},null, null, now(), '0')")
    void createStory( String id, String senderId, String text, String image, String video);

    /**
     * 删除动态
     */
    @Update("update story set del_flag = '1' where id = #{id} and senderId = #{userId}")
    void deleteStory(String id,String userId);

    /**
     * 根据userId获取动态
     */
    @Select("select id, senderId, text, image, video, like_count, liker, comment, create_date from story where senderId = #{userId} and del_flag = '0' order by create_date desc")
    List<Story> getStoryListById(String userId);

    /**
     * 根据storyId获取评论
     */
    @Select("select id, storyId, senderId, text, create_date from comment where storyId = #{storyId} and del_flag = '0' order by create_date")
    List<Comment> getCommentListById(String storyId);

    /**
     * 发表评论
     */
    @Insert("insert into comment(id, storyId, senderId, text, create_date, del_flag) values(#{id}, #{storyId}, #{senderId}, #{text}, now(), '0')")
    void createComment(String id,String storyId,String senderId,String text);

    /**
     * 喜欢动态 + 1
     */
    @Update("update story set like_count = like_count + 1 where id = #{storyId}")
    void likeStory(String storyId);

    /**
     * 取消喜欢动态
     */
    @Update("update story set like_count = like_count - 1 where id = #{storyId}")
    void unlikeStory(String storyId);

    /**获取喜欢者*/
    @Select("select liker from story where id = #{storyId} and del_flag = '0'")
    String getLiker(String storyId);

    /**更新喜欢者*/
    @Update("update story set liker = #{liker} where id = #{storyId}")
    void updateLiker(String storyId,String liker);
}