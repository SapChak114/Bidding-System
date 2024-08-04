package com.biding.product.repository;

import com.biding.product.dao.Vendors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorsRepository extends JpaRepository<Vendors, Integer> {
}
