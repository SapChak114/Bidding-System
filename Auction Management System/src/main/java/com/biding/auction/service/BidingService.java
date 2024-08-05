package com.biding.auction.service;

public interface BidingService {
    public void receiveMessage(String message);

    public void determineAuctionWinners();
}
