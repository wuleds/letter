package cn.wule.letter.channel.controller;
//汉江师范学院 数计学院 吴乐创建于2024 4月 12 23:24

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/channel")
@Slf4j
public class ChannelController
{
    /**创建频道*/
    @RequestMapping("/create")
    public void createChannel()
    {
        log.info("创建频道");
    }

    /**关注频道*/
    @RequestMapping("/join")
    public void joinChannel()
    {
        log.info("加入频道");
    }

    /**取消关注频道*/
    @RequestMapping("/quit")
    public void quitChannel()
    {
        log.info("退出频道");
    }

    /**关闭频道*/
    @RequestMapping("/dismiss")
    public void dismissChannel()
    {
        log.info("解散频道");
    }

    /**获取频道信息*/
    @RequestMapping("/info")
    public void getChannelInfo()
    {
        log.info("获取频道信息");
    }

    /**获取频道的关注者列表*/
    @RequestMapping("/members")
    public void getChannelMembers()
    {
        log.info("获取频道成员");
    }
}