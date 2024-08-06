package com.biding.auction.winningStrategy.strategies;

import com.biding.auction.dao.Bid;
import com.biding.auction.winningStrategy.WinnerDeterminationStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class HighestAmountAlphabeticalUserNameStrategy implements WinnerDeterminationStrategy {
    @Override
    public Optional<Bid> determineWinner(List<Bid> bids) {
        return bids.stream()
                .max(Comparator.comparing(Bid::getAmount)
                        .thenComparing(bid -> bid.getUser().getName()));
    }
}
