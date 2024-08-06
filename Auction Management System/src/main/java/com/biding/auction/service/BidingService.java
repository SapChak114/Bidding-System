package com.biding.auction.service;

import com.biding.auction.dao.Auction;

import java.util.Optional;

public interface BidingService {
    public void receiveMessage(String message);

    public void determineAuctionWinners();

    Boolean findByAuctionPresent(Optional<Auction> auction);
}
