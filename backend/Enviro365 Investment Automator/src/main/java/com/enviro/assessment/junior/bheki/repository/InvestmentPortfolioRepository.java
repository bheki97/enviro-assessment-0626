package com.enviro.assessment.junior.bheki.repository;

import com.enviro.assessment.junior.bheki.entity.InvestmentPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvestmentPortfolioRepository extends JpaRepository<InvestmentPortfolio, String> {

    Optional<InvestmentPortfolio> findByEmail(String email);
}
