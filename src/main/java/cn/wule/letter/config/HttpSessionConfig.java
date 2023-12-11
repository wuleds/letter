package cn.wule.letter.config;
//汉江师范学院 数计学院 吴乐创建于2023/12/11 22:44

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpSessionConfig extends ServerEndpointConfig.Configurator
{
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response)
    {
        //获取HttpSession对象
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        //将HttpSession对象存储到配置对象中。
        sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}