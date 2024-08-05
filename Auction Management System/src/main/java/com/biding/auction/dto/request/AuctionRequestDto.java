package com.biding.auction.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class AuctionRequestDto {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Bidding start time is required")
    @FutureOrPresent(message = "Bidding start time must be in the present or future")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String biddingStartTime;

    @NotNull(message = "Bidding end time is required")
    @FutureOrPresent(message = "Bidding end time must be in the future")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String biddingEndTime;

}