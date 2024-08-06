package com.biding.auction.config;

import com.biding.auction.dao.User;
import com.biding.auction.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Base64;
import java.util.Map;
import java.util.Objects;

import static com.biding.auction.constants.WebSocketConstants.AUTHORIZATION;
import static com.biding.auction.constants.WebSocketConstants.BASIC;

@Component
@Order(1)
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final UserRepository userRepository;

    @Autowired
    public WebSocketAuthInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request.getHeaders().get(AUTHORIZATION) == null || request.getHeaders().get(AUTHORIZATION).size() == 0) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        String authHeader = Objects.requireNonNull(request.getHeaders().get(AUTHORIZATION)).get(0);

        if (!authHeader.startsWith(BASIC)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        String[] credentials = new String(Base64.getDecoder().decode(authHeader.substring(6))).split(":");
        String email = credentials[0];
        User user = userRepository.findByEmail(email);

        if (Objects.isNull(user) || !BCrypt.checkpw(credentials[1], user.getPassword())) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}