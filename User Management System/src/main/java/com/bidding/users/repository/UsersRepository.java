package com.bidding.users.repository;

import com.bidding.users.dao.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE " +
            "(:name IS NULL OR u.name = :name) AND " +
            "(:email IS NULL OR u.email = :email) AND " +
            "(:contact IS NULL OR u.contact = :contact)")
    Page<User> findByOptions(Pageable pageable,
                             @Param("name") String name,
                             @Param("email") String email,
                             @Param("contact") String contact);

    Optional<User> findByEmail(String email);
}
