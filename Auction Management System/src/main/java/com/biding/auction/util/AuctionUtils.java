package com.biding.auction.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
}
