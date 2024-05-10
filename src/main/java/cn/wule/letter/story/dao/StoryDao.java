package cn.wule.letter.story.dao;

import cn.wule.letter.story.model.Story;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StoryDao
{
    /**
     * 创建动态
     */
    @Insert("insert into story(id, senderId, text, image, video, like_count, liker, comment, create_date, del_flag) values(#{id}, #{senderId}, #{text}, #{image}, #{video}, null,null, null, now(), '0')")
    void createStory( String id, String senderId, String text, String image, String video);

    /**
     * 删除动态
     */
    @Update("update story set del_flag = '1' where id = #{id} and senderId = #{userId}")
    void deleteStory(String id,String userId);

    /**
     * 根据userId获取动态
     */
    @Select("select id, senderId, text, image, video, like_count, liker, comment, create_date from story where senderId = #{userId} and del_flag = '0'")
    List<Story> getStoryListById(String userId);

}