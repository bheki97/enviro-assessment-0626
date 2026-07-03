package com.enviro.assessment.junior.bheki.mapper;

import com.enviro.assessment.junior.bheki.dto.InvestmentPortfolioResponse;
import com.enviro.assessment.junior.bheki.entity.InvestmentPortfolio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = ProductMapper.class)
public interface InvestmentPortfolioMapper {

    InvestmentPortfolioResponse toResponse(InvestmentPortfolio investmentPortfolio);
}
