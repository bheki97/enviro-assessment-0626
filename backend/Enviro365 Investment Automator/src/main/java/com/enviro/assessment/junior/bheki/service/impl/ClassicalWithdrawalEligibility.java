package com.enviro.assessment.junior.bheki.service.impl;

import com.enviro.assessment.junior.bheki.entity.Product;
import com.enviro.assessment.junior.bheki.enumerate.ProductType;
import com.enviro.assessment.junior.bheki.exception.ApiException;
import com.enviro.assessment.junior.bheki.service.WithdrawalEligibility;
import org.springframework.stereotype.Service;

@Service
public class ClassicalWithdrawalEligibility implements WithdrawalEligibility {


    @Override
    public boolean isEligible(Product product, double amount) {

        double balance = product.getBalance();
        if(balance <=amount) {
            throw new ApiException("Withdrawal must not exceed Balance");
        }
        if((balance*0.9)<= amount) {
            throw new ApiException("Withdrawal must not exceed 90% of Balance");
        }

        if(ProductType.RETIREMENT.equals(product.getProductType()) &&
                product.getInvestmentPortfolio().getAge()<=65){
            throw new ApiException("Retirement withdrawals only allowed if age older 65");
        }

        return true;
    }
}
