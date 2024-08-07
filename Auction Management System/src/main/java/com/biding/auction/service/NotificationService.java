package com.biding.auction.service;

import com.biding.auction.dao.Auction;
import com.biding.auction.dao.User;

public interface NotificationService {
    void sendNotification(User winner, Auction auction);
    void notificationFallback(User winner, Auction auction, Throwable throwable);
}
