package com.enviro.assessment.junior.bheki.service;


import com.enviro.assessment.junior.bheki.entity.InvestmentPortfolio;
import com.enviro.assessment.junior.bheki.entity.Product;
import com.enviro.assessment.junior.bheki.enumerate.ProductType;
import com.enviro.assessment.junior.bheki.exception.ApiException;
import com.enviro.assessment.junior.bheki.service.impl.ClassicalWithdrawalEligibility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WithdrawalEligibilityTests {

    private ClassicalWithdrawalEligibility withdrawalEligibility;

    @BeforeEach
    void setUp() {
        withdrawalEligibility = new ClassicalWithdrawalEligibility();
    }

    @Test
    void isEligible_ShouldReturnTrue_WhenWithdrawalIsValid() {


        Product product = new Product();
        product.setBalance(10000);
        product.setProductType(ProductType.CASH);


        boolean result = withdrawalEligibility.isEligible(product, 5000);


        assertTrue(result);
    }

    @Test
    void isEligible_ShouldThrowException_WhenWithdrawalExceedsBalance() {

        Product product = new Product();
        product.setBalance(10000);
        product.setProductType(ProductType.CASH);


        ApiException exception = assertThrows(
                ApiException.class,
                () -> withdrawalEligibility.isEligible(product, 10000)
        );

        assertEquals(
                "Withdrawal must not exceed Balance",
                exception.getMessage()
        );
    }

    @Test
    void isEligible_ShouldThrowException_WhenWithdrawalExceedsNinetyPercent() {

        Product product = new Product();
        product.setBalance(10000);
        product.setProductType(ProductType.CASH);


        ApiException exception = assertThrows(
                ApiException.class,
                () -> withdrawalEligibility.isEligible(product, 9500)
        );

        assertEquals(
                "Withdrawal must not exceed 90% of Balance",
                exception.getMessage()
        );
    }

    @Test
    void isEligible_ShouldThrowException_WhenRetirementAgeIsSixtyFiveOrYounger() {

        InvestmentPortfolio portfolio = new InvestmentPortfolio();
        portfolio.setAge(60);

        Product product = new Product();
        product.setBalance(10000);
        product.setProductType(ProductType.RETIREMENT);
        product.setInvestmentPortfolio(portfolio);


        ApiException exception = assertThrows(
                ApiException.class,
                () -> withdrawalEligibility.isEligible(product, 5000)
        );

        assertEquals(
                "Retirement withdrawals only allowed if age older 65",
                exception.getMessage()
        );
    }

    @Test
    void isEligible_ShouldReturnTrue_WhenRetirementAgeIsGreaterThanSixtyFive() {

        InvestmentPortfolio portfolio = new InvestmentPortfolio();
        portfolio.setAge(70);

        Product product = new Product();
        product.setBalance(10000);
        product.setProductType(ProductType.RETIREMENT);
        product.setInvestmentPortfolio(portfolio);


        boolean result = withdrawalEligibility.isEligible(product, 5000);


        assertTrue(result);
    }

}
