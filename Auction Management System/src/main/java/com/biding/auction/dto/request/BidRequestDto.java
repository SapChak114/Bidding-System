package com.biding.auction.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidRequestDto {
    private Long userId;
    private Long auctionId;
    private Double amount;
}