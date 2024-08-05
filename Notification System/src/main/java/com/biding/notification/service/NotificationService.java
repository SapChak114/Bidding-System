package com.biding.notification.service;

import com.biding.notification.dtos.request.NotificationRequestDto;

public interface NotificationService {
    void notifyUser(NotificationRequestDto notificationRequestDto);
}
