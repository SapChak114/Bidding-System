package com.biding.auction.repository;

import com.biding.auction.dao.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
