package com.enviro.assessment.junior.bheki.repository;


import com.enviro.assessment.junior.bheki.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findAllByInvestmentPortfolioId(String investmentPortfolioId);
}
