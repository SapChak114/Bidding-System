package com.biding.auction.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderClassName = "builder")
public class NotificationRequestDto {
    private String userName;
    private String userEmail;
    private String userContact;
    private String productName;
    private Long auctionId;
}