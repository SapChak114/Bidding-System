package com.biding.auction.util;

import com.biding.auction.enums.BiddingStrategy;
import com.biding.auction.winningStrategy.WinnerDeterminationStrategy;
import com.biding.auction.winningStrategy.strategies.HighestAmountAlphabeticalUserNameStrategy;
import com.biding.auction.winningStrategy.strategies.HighestAmountRandomBidStrategy;
import com.biding.auction.winningStrategy.strategies.HighestBidAmountEarliestFirstStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.biding.auction.enums.BiddingStrategy.*;

public class AuctionUtils {

    public static Date converStringDateToDate(String dateString){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final Map<BiddingStrategy, WinnerDeterminationStrategy> biddingStrategiesMap =
            Map.of(EARLY_DATE, new HighestBidAmountEarliestFirstStrategy(),
            ALPHABETIC, new HighestAmountAlphabeticalUserNameStrategy(),
            RANDOM, new HighestAmountRandomBidStrategy());
}
