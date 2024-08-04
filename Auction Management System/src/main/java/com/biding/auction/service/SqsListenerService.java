package com.biding.auction.service;

import com.biding.auction.constants.SQSConstants;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Service;

@Service
public class SqsListenerService {
    @SqsListener(value = SQSConstants.SQS_QUEUE_NAME)
    public void receiveMessage(String message) {
        // Process the message
        System.out.println("Received message: " + message);
    }
}