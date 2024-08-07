package com.biding.auction.dto.response;

import com.biding.auction.enums.BiddingStrategy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuctionResponseDto {

    private Long id;
    private ProductResponseDto product;
    private Date biddingStartTime;
    private Date biddingEndTime;
    private UserResponseDto winner; // Can be null if no winner is assigned
    private BiddingStrategy biddingStrategy;

    public AuctionResponseDto(){}
    public AuctionResponseDto(Long id, ProductResponseDto product, Date biddingStartTime, Date biddingEndTime, UserResponseDto winner, BiddingStrategy biddingStrategy) {
        this.id = id;
        this.product = product;
        this.biddingStartTime = biddingStartTime;
        this.biddingEndTime = biddingEndTime;
        this.winner = winner;
        this.biddingStrategy = biddingStrategy;
    }

}