package com.biding.auction.dao;

import com.biding.auction.dto.model.BaseModel;
import com.biding.auction.enums.BiddingStrategy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;


@Entity
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Auction extends BaseModel {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonManagedReference
    private Product product;

    @Column(nullable = false)
    private Date biddingStartTime;

    @Column(nullable = false)
    private Date biddingEndTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BiddingStrategy biddingStrategy;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)  // Nullable initially
    private User winner;
}
