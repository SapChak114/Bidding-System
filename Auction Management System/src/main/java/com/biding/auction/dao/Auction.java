package com.biding.auction.dao;

import com.biding.auction.dto.model.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
public class Auction extends BaseModel {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Date biddingStartTime;

    @Column(nullable = false)
    private Date biddingEndTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)  // Nullable initially
    private User winner;
}