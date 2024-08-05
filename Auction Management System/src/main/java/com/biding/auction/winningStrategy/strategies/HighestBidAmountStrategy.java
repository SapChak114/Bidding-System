package com.biding.auction.winningStrategy.strategies;

import com.biding.auction.dao.Bid;
import com.biding.auction.winningStrategy.WinnerDeterminationStrategy;

import java.util.List;
import java.util.Optional;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class HighestBidAmountStrategy implements WinnerDeterminationStrategy {
    @Override
    public Optional<Bid> determineWinner(List<Bid> bids) {
        return bids.stream()
            .collect(Collectors.groupingBy(Bid::getAmount))
            .entrySet().stream()
            .max(Comparator.comparingDouble(e -> e.getKey()))  // Highest bid amount
            .flatMap(maxBidAmountEntry -> {
                List<Bid> highestBids = maxBidAmountEntry.getValue();
                if (highestBids.size() == 1) {
                    return highestBids.stream().findFirst();  // Single highest bid
                } else {
                    return Optional.empty(); // Conflict, need further strategy
                }
            });
    }
}
