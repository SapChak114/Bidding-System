package com.biding.auction.winningStrategy;

import com.biding.auction.dao.Bid;
import com.biding.auction.enums.BiddingStrategy;
import com.biding.auction.winningStrategy.strategies.HighestAmountAlphabeticalUserNameStrategy;
import com.biding.auction.winningStrategy.strategies.HighestAmountRandomBidStrategy;
import com.biding.auction.winningStrategy.strategies.HighestBidAmountEarliestFirstStrategy;

import static com.biding.auction.util.AuctionUtils.*;

import java.util.List;
import java.util.Optional;

import static com.biding.auction.constants.AuctionConstant.*;

public class WinnerDeterminationContext {
    private final WinnerDeterminationStrategy strategy;

    public WinnerDeterminationContext(BiddingStrategy strategyKey) {
        strategy = biddingStrategiesMap.computeIfAbsent(strategyKey, key -> {
            throw new RuntimeException(INVALID_BIDING_STRATEGY);});
    }

    public Optional<Bid> determineWinner(List<Bid> bids) {
        return strategy.determineWinner(bids);
    }
}
