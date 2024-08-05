package com.biding.notification.service.impl;


import com.biding.notification.dao.Notification;
import com.biding.notification.dtos.request.NotificationRequestDto;
import com.biding.notification.repository.NotificationRepository;
import com.biding.notification.service.EmailService;
import com.biding.notification.service.NotificationService;
import com.biding.notification.service.SmsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.biding.notification.constants.NotificationConstants.SMS_BODY;
import static com.biding.notification.constants.NotificationConstants.EMAIL_BODY;
import static com.biding.notification.constants.NotificationConstants.EMAIL_SUBJECT;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final EmailService emailService;

    private final SmsService smsService;

    private final ObjectMapper mapper;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, EmailService emailService, SmsService smsService) {
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
        this.smsService = smsService;
        this.mapper = new ObjectMapper();
    }
    @Override
    public void notifyUser(NotificationRequestDto notificationRequestDto) {

        String smsBody = String.format(SMS_BODY,
                notificationRequestDto.getUserName(),
                notificationRequestDto.getAuctionId(),
                notificationRequestDto.getProductName());

        String emailBody = String.format(EMAIL_BODY,
                notificationRequestDto.getUserName(),
                notificationRequestDto.getProductName(),
                notificationRequestDto.getAuctionId()
        );

        String toNumber = notificationRequestDto.getUserContact();
        String toEmail = notificationRequestDto.getUserEmail();

        smsService.sendSms(toNumber, smsBody);
        emailService.sendSimpleEmail(toEmail, EMAIL_SUBJECT, emailBody);

        // Save Notification to Repository
        Notification notification = mapper.convertValue(notificationRequestDto, Notification.class);
        notificationRepository.save(notification);
    }

}
