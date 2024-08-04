package com.biding.auction.repository;

import com.biding.auction.dao.Bids;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidsRepository extends JpaRepository<Bids, Long> {
}
