package com.biding.auction.enums;

import com.biding.auction.constants.AuctionConstant;
public enum BiddingStrategy {
    EARLY_DATE,
    ALPHABETIC,
    RANDOM;

    public static BiddingStrategy getByName(String biddingStrategy) {
        switch (biddingStrategy.toUpperCase()) {
            case AuctionConstant.EARLY_DATE -> {
                return EARLY_DATE;
            }
            case AuctionConstant.ALPHABETIC -> {
                return ALPHABETIC;
            }
            case AuctionConstant.RANDOM -> {
                return RANDOM;
            }
            default -> {
                return null;
            }
        }
    }
}
