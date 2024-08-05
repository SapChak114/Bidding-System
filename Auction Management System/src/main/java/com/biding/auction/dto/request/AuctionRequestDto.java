package com.biding.auction.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuctionRequestDto {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Bidding start time is required")
    @FutureOrPresent(message = "Bidding start time must be in the present or future")
    private Date biddingStartTime;

    @NotNull(message = "Bidding end time is required")
    @FutureOrPresent(message = "Bidding end time must be in the future")
    private Date biddingEndTime;

    private Long winnerId; // Can be null initially
}