package com.biding.vendor.repository;

import com.biding.vendor.dao.Vendors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendors, Integer> {

    @Query("SELECT v FROM Vendors v WHERE " +
            "(:name IS NULL OR v.name=:name) AND" +
            "(:contact IS NULL OR v.contact=:contact) AND" +
            "(:email IS NULL OR v.email=:email)")
    Page<Vendors> findByOptions(Pageable pageable,
                                @Param("name") String name,
                                @Param("contact") String contact,
                                @Param("email") String email);

    @Query("SELECT v FROM Vendors v WHERE v.email=:email")
    Vendors findByEmail(@Param("email") String email);
}
