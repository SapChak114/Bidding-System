package com.biding.auction.winningStrategy.strategies;

import com.biding.auction.dao.Bid;
import com.biding.auction.winningStrategy.WinnerDeterminationStrategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Comparator;
import java.util.stream.Collectors;

public class HighestBidAmountEarliestFirstStrategy implements WinnerDeterminationStrategy {
    @Override
    public Optional<Bid> determineWinner(List<Bid> bids) {
        return bids.stream()
                .collect(Collectors.groupingBy(Bid::getAmount))
                .entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getKey))  // Highest bid amount
                .flatMap(maxBidAmountEntry -> {
                    List<Bid> highestBids = maxBidAmountEntry.getValue();
                    return highestBids.stream()
                            .min(Comparator.comparing(Bid::getCreatedAt));  // Sort by createdAt and get the earliest
                });
    }
}
