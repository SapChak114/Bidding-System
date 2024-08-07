package com.biding.auction.winningStrategy;

import com.biding.auction.dao.Bid;
import com.biding.auction.enums.BiddingStrategy;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.biding.auction.constants.AuctionConstant.INVALID_BIDING_STRATEGY;
import static com.biding.auction.util.AuctionUtils.biddingStrategiesMap;

public class WinnerDeterminationContext {
    private final WinnerDeterminationStrategy strategy;

    public WinnerDeterminationContext(BiddingStrategy strategyKey) {
        strategy = biddingStrategiesMap.get(strategyKey);
        if (Objects.isNull(strategy)) {
            throw new RuntimeException(INVALID_BIDING_STRATEGY);
        }
    }

    public Optional<Bid> determineWinner(List<Bid> bids) {
        return strategy.determineWinner(bids);
    }
}
