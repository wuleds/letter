package cn.wule.letter.connect;
//汉江师范学院 数计学院 吴乐创建于2024 4月 10 01:40

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<String, WebSocketSession> wsSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //TODO 连接前进行身份验证
        String account = session.getPrincipal().getName();
        wsSessions.put(account, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //TODO 处理接收到的消息
        session.sendMessage(message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //TODO 处理连接关闭
        String account = session.getPrincipal().getName();
        wsSessions.remove(account);
    }

    public void sendMessageToUser(String userId, String message) throws Exception {
        //TODO 发送消息给指定用户
        WebSocketSession session = wsSessions.get(userId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }
}