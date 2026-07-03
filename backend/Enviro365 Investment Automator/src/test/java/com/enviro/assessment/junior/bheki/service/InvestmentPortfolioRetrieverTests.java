package com.enviro.assessment.junior.bheki.service;


import com.enviro.assessment.junior.bheki.dto.InvestmentPortfolioResponse;
import com.enviro.assessment.junior.bheki.entity.InvestmentPortfolio;
import com.enviro.assessment.junior.bheki.exception.ApiException;
import com.enviro.assessment.junior.bheki.mapper.InvestmentPortfolioMapper;
import com.enviro.assessment.junior.bheki.repository.InvestmentPortfolioRepository;
import com.enviro.assessment.junior.bheki.service.impl.InvestmentPortfolioRetrieverImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvestmentPortfolioRetrieverTests {
    @Mock
    private InvestmentPortfolioRepository investmentPortfolioRepository;

    @Mock
    private InvestmentPortfolioMapper investmentPortfolioMapper;

    @InjectMocks
    private InvestmentPortfolioRetrieverImpl investmentPortfolioRetriever;

    @Test
    void retrieveInvestmentPortfolio_ShouldReturnResponse_WhenPortfolioExists() {
        // Given
        String email = "john@example.com";

        InvestmentPortfolio portfolio = new InvestmentPortfolio();
        InvestmentPortfolioResponse expectedResponse = new InvestmentPortfolioResponse();

        when(investmentPortfolioRepository.findByEmail(email))
                .thenReturn(Optional.of(portfolio));

        when(investmentPortfolioMapper.toResponse(portfolio))
                .thenReturn(expectedResponse);

        // When
        InvestmentPortfolioResponse actualResponse =
                investmentPortfolioRetriever.retrieveInvestmentPortfolio(email);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);

        verify(investmentPortfolioRepository).findByEmail(email);
        verify(investmentPortfolioMapper).toResponse(portfolio);

        verifyNoMoreInteractions(
                investmentPortfolioRepository,
                investmentPortfolioMapper
        );
    }

    @Test
    void retrieveInvestmentPortfolio_ShouldThrowApiException_WhenPortfolioDoesNotExist() {
        // Given
        String email = "john@example.com";

        when(investmentPortfolioRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        // When & Then
        ApiException exception = assertThrows(
                ApiException.class,
                () -> investmentPortfolioRetriever.retrieveInvestmentPortfolio(email)
        );

        assertEquals(
                "Investment Portfolio does not exist",
                exception.getMessage()
        );

        verify(investmentPortfolioRepository).findByEmail(email);
        verifyNoInteractions(investmentPortfolioMapper);
    }


}
