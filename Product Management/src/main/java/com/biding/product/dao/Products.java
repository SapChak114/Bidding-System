package com.biding.product.dao;

import com.biding.product.dto.model.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class Products extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendors vendors;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double basePrice;

    @Column(nullable = false)
    private Date biddingStartTime;

    @Column(nullable = false)
    private Date biddingEndTime;
}
