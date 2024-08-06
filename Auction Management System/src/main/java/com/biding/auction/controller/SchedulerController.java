package com.biding.auction.controller;

import com.biding.auction.service.BidingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
public class SchedulerController {
    private final BidingService bidingService;

    @Autowired
    public SchedulerController(BidingService bidingService) {
        this.bidingService = bidingService;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)  //TODO : As of now 1 hour interval is scheduled
    public void findWinner() {
        log.info("Starting winner determination process");
        try {
            bidingService.determineAuctionWinners();
        } catch (Exception e) {
            log.error("Error during winner determination process: {}", e.getMessage(), e);
        }
    }
}
