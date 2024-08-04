package com.biding.product.repository;

import com.biding.product.dao.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {


    @Query(value = "SELECT * FROM products p " +
            "WHERE (:name IS NULL OR p.name = :name) " +
            "AND (:description IS NULL OR p.description = :description) " +
            "AND (:basePriceMin IS NULL OR p.base_price >= :basePriceMin) " +
            "AND (:basePriceMax IS NULL OR p.base_price <= :basePriceMax) " +
            "AND (:vendorId IS NULL OR p.vendor_id = :vendorId)",
            nativeQuery = true)
    Page<Product> findByFilters(Pageable pageable,
                                @Param("name") String name,
                                @Param("description") String description,
                                @Param("basePriceMin") Double basePriceMin,
                                @Param("basePriceMax") Double basePriceMax,
                                @Param("vendorId") Long vendorId);

}
