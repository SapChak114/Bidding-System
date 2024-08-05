package com.biding.auction.winningStrategy;

import com.biding.auction.dao.Bid;

import java.util.List;
import java.util.Optional;

public interface WinnerDeterminationStrategy {
    Optional<Bid> determineWinner(List<Bid> bids);
}
