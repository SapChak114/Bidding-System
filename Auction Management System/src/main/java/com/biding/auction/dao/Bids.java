package com.biding.auction.dao;

import com.biding.auction.dto.model.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "bids", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "auction_id"}))
public class Bids extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @Column(nullable = false)
    private Double amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bids bid = (Bids) o;

        if (!user.equals(bid.user)) return false;
        return auction.equals(bid.auction);
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + auction.hashCode();
        return result;
    }

}