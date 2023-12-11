package cn.wule.letter.config;
//汉江师范学院 数计学院 吴乐创建于2023/12/11 22:41

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

//@Configuration
public class WebsocketConfig {

    /**
     * 注入ServerEndpointExporter的bean对象，自动注册使用@ServerEndpoint注解的bean
     */
    //@Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}