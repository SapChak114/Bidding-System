package com.biding.auction.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import static com.biding.auction.constants.WebSocketConstants.*;
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final MyWebSocketHandler myWebSocketHandler;

    private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    @Autowired
    public WebSocketConfig(MyWebSocketHandler myWebSocketHandler, WebSocketAuthInterceptor webSocketAuthInterceptor) {
        this.myWebSocketHandler = myWebSocketHandler;
        this.webSocketAuthInterceptor = webSocketAuthInterceptor;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler, WEBSOCKET_PATH)
                .addInterceptors(webSocketAuthInterceptor)
                .setAllowedOrigins("*");
    }

}