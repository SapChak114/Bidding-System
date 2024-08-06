package com.biding.auction.winningStrategy.strategies;

import com.biding.auction.dao.Bid;
import com.biding.auction.winningStrategy.WinnerDeterminationStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class HighestAmountRandomBidStrategy implements WinnerDeterminationStrategy {
    private final Random random = new Random();

    @Override
    public Optional<Bid> determineWinner(List<Bid> bids) {
        if (bids.isEmpty()) {
            return Optional.empty();
        }

        // Find the maximum bid amount
        double maxAmount = bids.stream()
                .max(Comparator.comparing(Bid::getAmount))
                .get()
                .getAmount();

        // Filter bids with the maximum bid amount
        List<Bid> maxBids = bids.stream()
                .filter(bid -> bid.getAmount() == maxAmount)
                .toList();

        // If there's only one bid with the maximum amount, return it
        if (maxBids.size() == 1) {
            return Optional.of(maxBids.get(0));
        }

        // Select a random bid from the filtered list
        return maxBids.stream()
                .skip(random.nextInt(maxBids.size()))
                .findFirst();
    }
}
