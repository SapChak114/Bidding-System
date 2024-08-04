package com.biding.auction.controller;

import com.biding.auction.service.AuctionService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuctionController {

    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }
}
