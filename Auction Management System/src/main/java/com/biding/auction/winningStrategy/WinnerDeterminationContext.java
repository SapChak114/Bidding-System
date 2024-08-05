package com.biding.auction.winningStrategy;

import com.biding.auction.dao.Bid;

import java.util.List;
import java.util.Optional;

public class WinnerDeterminationContext {
    private final WinnerDeterminationStrategy strategy;

    public WinnerDeterminationContext(WinnerDeterminationStrategy strategy) {
        this.strategy = strategy;
    }

    public Optional<Bid> determineWinner(List<Bid> bids) {
        return strategy.determineWinner(bids);
    }
}
