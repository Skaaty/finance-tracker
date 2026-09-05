package com.skaaty.financetracker.repository;

import com.skaaty.financetracker.model.Portfolio;
import com.skaaty.financetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByUserId(Long userId);

    Long user(User user);
}
