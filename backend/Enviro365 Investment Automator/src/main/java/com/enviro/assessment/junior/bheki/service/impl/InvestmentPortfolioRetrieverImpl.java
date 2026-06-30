package com.enviro.assessment.junior.bheki.service.impl;

import com.enviro.assessment.junior.bheki.dto.InvestmentPortfolioResponse;
import com.enviro.assessment.junior.bheki.entity.InvestmentPortfolio;
import com.enviro.assessment.junior.bheki.exception.ApiException;
import com.enviro.assessment.junior.bheki.mapper.InvestmentPortfolioMapper;
import com.enviro.assessment.junior.bheki.repository.InvestmentPortfolioRepository;
import com.enviro.assessment.junior.bheki.service.InvestmentPortfolioRetriever;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvestmentPortfolioRetrieverImpl implements InvestmentPortfolioRetriever {

    private final InvestmentPortfolioRepository investmentPortfolioRepository;
    private final InvestmentPortfolioMapper investmentPortfolioMapper;

    @Override
    public InvestmentPortfolioResponse retrieveInvestmentPortfolio(String email) {
        InvestmentPortfolio investmentPortfolio = investmentPortfolioRepository
                .findByEmail(email)
                .orElseThrow(()-> new ApiException("Investment Portfolio does not exist"));


       return investmentPortfolioMapper.toResponse(investmentPortfolio);

    }
}
