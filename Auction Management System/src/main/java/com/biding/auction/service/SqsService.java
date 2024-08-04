package com.biding.auction.service;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.biding.auction.constants.SQSConstants.SQS_QUEUE_NAME;


@Service
public class SqsService {
    private SqsTemplate sqsTemplate;

    @Autowired
    public SqsService(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    public void sendMessage(String message) {
        sqsTemplate.send(to -> to.queue(SQS_QUEUE_NAME).payload(message));
    }

}