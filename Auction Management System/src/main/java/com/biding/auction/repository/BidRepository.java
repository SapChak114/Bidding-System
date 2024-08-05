package com.biding.auction.repository;

import com.biding.auction.dao.Auction;
import com.biding.auction.dao.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByAuctionId(Long auctionId);
    Optional<List<Bid>> findByAuction(Auction auction);
}
