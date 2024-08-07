package com.biding.auction.service.impl;

import com.biding.auction.dao.Auction;
import com.biding.auction.dao.User;
import com.biding.auction.dto.request.NotificationRequestDto;
import com.biding.auction.service.NotificationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.biding.auction.constants.AuctionConstant.NOTIFICATION_URL;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final RestTemplate restTemplate;

    @Autowired
    public NotificationServiceImpl(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    @Retry(name = "default")
    public void sendNotification(User winner, Auction auction) {
        log.info("starting the send notification ");
        try {
            NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                    .userName(winner.getName())
                    .userEmail(winner.getEmail())
                    .userContact(winner.getContact())
                    .productName(auction.getProduct().getName())
                    .auctionId(auction.getId())
                    .build();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

            HttpEntity<NotificationRequestDto> request = new HttpEntity<>(notificationRequestDto, headers);
            restTemplate.exchange(
                    NOTIFICATION_URL,
                    HttpMethod.POST,
                    request,
                    String.class);
        } catch (Exception e){
            log.error("Exception while calling notification service :: {} ", e.getMessage());
            throw new RuntimeException("Failed to send notification");
        }
    }

    @Override
    public void notificationFallback(User winner, Auction auction, Throwable throwable) {
        log.info("Retries exhausted for communicating to user: {} for auction id: {}. Exception: {}",
                winner.getName(), auction.getId(), throwable.getMessage());
    }
}
