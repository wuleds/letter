package cn.wule.letter.connect.controller;
//汉江师范学院 数计学院 吴乐创建于2024 4月 10 02:21

import cn.wule.letter.connect.model.UserMessage;
import cn.wule.letter.model.user.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class MessageController
{
    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/server")
    public void processMessageFromClient(UserMessage message){
        //TODO 处理客户端发送的消息
        log.info("接收到客户端消息: {}", message);
        String userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        sendToUser(userId, "服务器回复: " + message.getText());
        log.info("发送消息给用户: {}", userId);
    }

    public void sendToUser(String userId, String message) {
        messagingTemplate.convertAndSendToUser(userId, "/get", message);
    }
}