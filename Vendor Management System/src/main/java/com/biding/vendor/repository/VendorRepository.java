package com.biding.vendor.repository;

import com.biding.vendor.dao.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    @Query("SELECT v FROM Vendor v WHERE " +
            "(:name IS NULL OR v.name=:name) AND" +
            "(:contact IS NULL OR v.contact=:contact) AND" +
            "(:email IS NULL OR v.email=:email)")
    Page<Vendor> findByOptions(Pageable pageable,
                               @Param("name") String name,
                               @Param("contact") String contact,
                               @Param("email") String email);

    @Query("SELECT v FROM Vendor v WHERE v.email=:email")
    Vendor findByEmail(@Param("email") String email);
}
