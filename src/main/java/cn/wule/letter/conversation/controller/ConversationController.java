package cn.wule.letter.conversation.controller;
//汉江师范学院 数计学院 吴乐创建于2024/3/13 0:14

import cn.wule.letter.conversation.model.Conversation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conversation")
public class ConversationController
{
    /**
     * 通过此请求新建对话
     * type 可能为private,group,channel。
     * 1.私聊：若已经新建过，则获取chatId，并获取历史记录，若新建，则获取chatId。
     * 2.群组：groupId即为chatId，获取chatId，并获取历史记录。
     * 3.频道：channelId即为chatId，获取chatId，并获取历史发布记录。
     * id 可能为userId,groupId,channelId,myId为当前用户的id。
     * data:{
     *     type:'',
     *     myId:'',
     *     id:''
     * }
     * */
    @PostMapping("/new")
    public String  newConversation(@RequestBody Conversation conversation) {
        String type = conversation.getType();
        String chatId = null;
        //判断类型
        //判断是否已经存在
        //返回chatId
        switch (type)
        {
            case "private":
                //判断是否已经存在,若已经存在，则获取chatId，并获取历史记录，若新建，则生成chatId。
                break;
            case "group":
                //直接查询chatId，然后返回。
                break;
            case "channel":
                //直接查询chatId，然后返回。
                break;
        }
        return chatId;
    }
}