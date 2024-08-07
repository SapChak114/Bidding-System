package com.biding.auction.winningStrategy;

import com.biding.auction.dao.Bid;
import com.biding.auction.enums.BiddingStrategy;
import com.biding.auction.winningStrategy.strategies.HighestAmountAlphabeticalUserNameStrategy;
import com.biding.auction.winningStrategy.strategies.HighestAmountRandomBidStrategy;
import com.biding.auction.winningStrategy.strategies.HighestBidAmountEarliestFirstStrategy;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.biding.auction.constants.AuctionConstant.INVALID_BIDING_STRATEGY;
import static com.biding.auction.enums.BiddingStrategy.*;

public class WinnerDeterminationContext {
    private final WinnerDeterminationStrategy strategy;
    public static final Map<BiddingStrategy, WinnerDeterminationStrategy> biddingStrategiesMap;
    static{
        biddingStrategiesMap = Map.of(EARLY_DATE, new HighestBidAmountEarliestFirstStrategy(),
                ALPHABETIC, new HighestAmountAlphabeticalUserNameStrategy(),
                RANDOM, new HighestAmountRandomBidStrategy());
    }

    public WinnerDeterminationContext(BiddingStrategy strategyKey) {
        strategy = biddingStrategiesMap.get(strategyKey);
        if (Objects.isNull(strategyKey)) {
            throw new RuntimeException(INVALID_BIDING_STRATEGY);
        }
    }

    public Optional<Bid> determineWinner(List<Bid> bids) {
        return strategy.determineWinner(bids);
    }
}
