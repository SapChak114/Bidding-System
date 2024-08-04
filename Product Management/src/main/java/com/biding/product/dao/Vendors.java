package com.biding.product.dao;

import com.biding.product.dto.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vendors extends BaseModel {

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String contact;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "vendors", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Products> products;
}
