package com.biding.auction.config;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import static com.biding.auction.constants.SQSConstants.SQS_QUEUE_NAME;

@Component
@Slf4j
public class MyWebSocketHandler extends TextWebSocketHandler {

    private final SqsTemplate sqsTemplate;

    @Autowired
    public MyWebSocketHandler(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String msg = message.getPayload();
        log.debug("Web Socket Message : {}", msg);
        sqsTemplate.send(to -> to.queue(SQS_QUEUE_NAME).payload(msg));
    }
}