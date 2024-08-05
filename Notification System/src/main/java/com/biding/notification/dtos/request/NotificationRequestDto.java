package com.biding.notification.dtos.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderClassName = "builder")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationRequestDto {
    private String userName;
    private String userEmail;
    private String userContact;
    private String productName;
    private Long auctionId;
}