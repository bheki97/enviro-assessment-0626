package com.enviro.assessment.junior.bheki.service;

import com.enviro.assessment.junior.bheki.dto.InvestmentPortfolioResponse;

public interface InvestmentPortfolioRetriever {

    InvestmentPortfolioResponse retrieveInvestmentPortfolio(String email);
}
