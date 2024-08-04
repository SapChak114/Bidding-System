package com.bidding.users.repository;

import com.bidding.users.dao.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
