package com.biding.auction.controller;

import com.biding.auction.service.SqsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sqs")
public class SqsController {

    private final SqsService sqsService;

    @Autowired
    public SqsController(SqsService sqsService) {
        this.sqsService = sqsService;
    }


    @PostMapping ("/")
    public String sendMessage(@RequestBody String message) {
        sqsService.sendMessage(message);
        return "Message sent";
    }

}