package com.skaaty.financetracker.repository;

import com.skaaty.financetracker.model.MonthlySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlySummaryRepository extends JpaRepository<MonthlySummary, Long > {

}
