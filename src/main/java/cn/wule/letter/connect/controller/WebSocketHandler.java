package cn.wule.letter.connect.controller;
//汉江师范学院 数计学院 吴乐创建于2024 4月 10 01:40

import cn.wule.letter.connect.model.MessageVo;
import cn.wule.letter.connect.model.ServerMessage;
import cn.wule.letter.connect.model.UnreadMessage;
import cn.wule.letter.connect.model.UserMessage;
import cn.wule.letter.connect.service.WebSocketService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
public class WebSocketHandler extends TextWebSocketHandler {
    //存储每个连接，key为用户ID，value为连接
    private final static ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    //存储每个连接的最后心跳时间，key为userId，value为时间戳
    private final static ConcurrentHashMap<String, Long> sessionLastHeartbeat = new ConcurrentHashMap<>();
    //存储每个用户的登录时间，key为userId，value为时间戳
    private final static ConcurrentHashMap<String, Long> userLoginTime = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static WebSocketService webSocketService;
    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        WebSocketHandler.webSocketService = webSocketService;
    }

    private final ObjectMapper om = new ObjectMapper();


    public WebSocketHandler() {
        //每1秒检查一次所有会话的最后心跳时间
        scheduler.scheduleAtFixedRate(this::checkHeartbeat, 1, 1, TimeUnit.SECONDS);
        //每 60秒检查一次所有会话的持续时间
        scheduler.scheduleAtFixedRate(this::checkLoginTime, 1, 60, TimeUnit.SECONDS);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("连接建立");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        UserMessage userMessage;
        try{
            userMessage= om.readValue(message.getPayload(), UserMessage.class);
        }catch (Exception e){
            log.error("消息解析失败");
            session.close();
            return;
        }
        String type = userMessage.getType();
        String Authorization = userMessage.getAuthorization();
        if(userMessage.existNull()) {
            log.error("消息不完整");
            session.close();
            return;
        }
        String userId = webSocketService.checkToken(Authorization);
        if(userId == null || !Objects.equals(userId,userMessage.getSender())) {
            session.sendMessage(new TextMessage(om.writeValueAsString(ServerMessage.builder().type("1").text("error").build())));
            session.close();
            log.error("用户验证失败");
            return;
        }

        switch (type) {
            case "0" -> {
                if (sessionLastHeartbeat.containsKey(userId)) {
                    sessionLastHeartbeat.replace(userId, System.currentTimeMillis());
                }else {
                    sessionLastHeartbeat.put(userId, System.currentTimeMillis());
                }
                session.sendMessage(new TextMessage(om.writeValueAsString(ServerMessage.builder().type("0").text("pong").build())));
            }
            case "1" -> {
                //存入session连接
                sessions.put(userId, session);
                if (sessionLastHeartbeat.containsKey(userId)) {
                    sessionLastHeartbeat.replace(userId, System.currentTimeMillis());
                }else {
                    sessionLastHeartbeat.put(userId, System.currentTimeMillis());
                }
                //存登录时间
                userLoginTime.put(userId, System.currentTimeMillis());
                log.info("用户 {} 连接成功", userId);
            }
            case "20" -> {
                //获取未读消息数量
                try {
                    List<UnreadMessage> unread = om.readValue(userMessage.getText(), new TypeReference<List<UnreadMessage>>() {});
                    unread = webSocketService.getUnreadMessageCount(userId, unread);
                    String unreadJson = om.writeValueAsString(unread);
                    session.sendMessage(new TextMessage(om.writeValueAsString(ServerMessage.builder().type("4").text(unreadJson).build())));
                }catch (Exception e){
                    log.error("未读数量，解析失败");
                    session.sendMessage(new TextMessage(om.writeValueAsString(ServerMessage.builder().type("3").text("获取未读消息的数量，失败").build())));
                }
            }
            case "21" -> {
                //获取当前对话的消息
                try {
                    //获取用户获取的最后一条消息的id
                    int lastMessageId = Integer.parseInt(userMessage.getText());
                    String chatId = userMessage.getChatId();
                    List<MessageVo> messages = webSocketService.getCurrentMessage(userId, chatId, lastMessageId);
                    String messagesJson = om.writeValueAsString(messages);
                    session.sendMessage(new TextMessage(om.writeValueAsString(ServerMessage.builder().type("5").text(messagesJson).chatId(chatId).chatType(webSocketService.getChatType(chatId)).build())));
                }catch (Exception e){
                    log.error("未读消息，解析失败");
                    session.sendMessage(new TextMessage(om.writeValueAsString(ServerMessage.builder().type("3").text("获取未读消息，失败").build())));
                }
            }
            default -> {
                //私聊消息，群聊消息，频道消息
                if(webSocketService.persistence(userMessage)){
                    session.sendMessage(new TextMessage(om.writeValueAsString(ServerMessage.builder().type("3").text("用户消息发送，成功").build())));
                }else {
                    session.sendMessage(new TextMessage(om.writeValueAsString(ServerMessage.builder().type("3").text("用户消息发送，失败").build())));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        // 当连接关闭时，移除对应的会话心跳记录
        log.info("连接关闭");
    }

    public void sendMessageToUser(WebSocketSession session, String message) throws Exception {
        //TODO 发送消息给指定用户
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }

    //检查超时心跳
    private void checkHeartbeat() {
        long now = System.currentTimeMillis();
        List<String> id = new ArrayList<>();
        sessionLastHeartbeat.forEach((userId, lastHeartbeatTime) -> {
            if ((now - lastHeartbeatTime) > 12000) { // 超过12秒没有心跳
                try {
                    id.add(userId);
                    WebSocketSession session = sessions.get(userId);
                    if (session != null && session.isOpen()) {
                        session.close();
                    }
                    log.info("{}的会话因心跳超时而关闭", userId);
                } catch (Exception e) {
                    log.error("关闭会话失败", e);
                }
            }
        });
        // 移除超时的会话
        id.forEach((userId) ->{
            sessions.remove(userId);
            sessionLastHeartbeat.remove(userId);
            userLoginTime.remove(userId);
        });
    }

    //检查过长时间的会话
    private void checkLoginTime() {
        long now = System.currentTimeMillis();
        List<String> id = new ArrayList<>();
        userLoginTime.forEach((userId, loginTime) -> {
            if ((now - loginTime) > 1800000) { // 会话时间超过 30 分钟
                try {
                    id.add(userId);
                    WebSocketSession session = sessions.get(userId);
                    if (session != null && session.isOpen()) {
                        session.close();
                    }
                    log.info("{}的会话因超时而关闭", userId);
                } catch (Exception e) {
                    log.error("关闭会话失败", e);
                }
            }
        });
        // 移除超时的会话
        id.forEach((userId) ->{
            sessions.remove(userId);
            sessionLastHeartbeat.remove(userId);
            userLoginTime.remove(userId);
        });
    }
}