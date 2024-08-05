package com.biding.auction.repository;

import com.biding.auction.dao.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    @Query(value = "SELECT * FROM auction WHERE product_id = :productId", nativeQuery = true)
    Optional<Auction> findAuctionByProductId(@Param("productId") Long productId);

    @Query("SELECT a FROM Auction a " +
            "JOIN a.product p " +
            "LEFT JOIN a.winner u " +
            "WHERE (:name IS NULL OR p.name = :name) " +
            "AND (:email IS NULL OR u.email = :email) " +
            "AND (:contact IS NULL OR u.contact = :contact)")
    Page<Auction> findByOptions(Pageable pageable,
                                @Param("name") String name,
                                @Param("email") String email,
                                @Param("contact") String contact);

    @Query(value = "SELECT * FROM auction a " +
            "WHERE a.bidding_end_time < :currentDate " +
            "AND a.winner_id IS NULL",
            nativeQuery = true)
    List<Auction> findAllByBiddingEndTimeBeforeAndWinnerIsNull(@Param("currentDate") Date currentDate);

    Page<Auction> findByWinnerIsNotNull(Pageable pageable);
}
