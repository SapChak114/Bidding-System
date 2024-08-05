package com.biding.auction.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuctionRequestDto {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Bidding start time is required")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String biddingStartTime;

    @NotNull(message = "Bidding end time is required")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String biddingEndTime;

}