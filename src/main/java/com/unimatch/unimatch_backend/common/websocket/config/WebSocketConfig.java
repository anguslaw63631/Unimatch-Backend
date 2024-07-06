package com.unimatch.unimatch_backend.common.websocket.config;

import com.unimatch.unimatch_backend.common.websocket.interceptor.SocketInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //Setup Stomp Endpoints to api path/websocket
        registry.addEndpoint("/socket").addInterceptors(new SocketInterceptor())
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // broadcast to All clients
        // one to one chat: simple, group chat: topic, invitation: invitation, msg revoke: msgRevoke
        config.enableSimpleBroker("/simple", "/topic", "/invitation", "/msgHandle");
        // Let Message Mapping to decide one to one or broadcast to all
        config.setApplicationDestinationPrefixes("/app", "/queue");
    }

}
