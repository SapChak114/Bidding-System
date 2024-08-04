package com.biding.auction.config;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import static com.biding.auction.constants.SQSConstants.SQS_QUEUE_NAME;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    private final SqsTemplate sqsTemplate;

    @Autowired
    public MyWebSocketHandler(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String msg = message.getPayload();
        System.out.println("Web Socket Message : "+msg);
        sqsTemplate.send(to -> to.queue(SQS_QUEUE_NAME).payload(msg));
    }
}