package com.biding.notification.controller;

import com.biding.notification.dtos.request.NotificationRequestDto;
import com.biding.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/")
    public void notify(NotificationRequestDto notificationRequestDto) {
        notificationService.notifyUser(notificationRequestDto);
    }
}
