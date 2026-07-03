package com.enviro.assessment.junior.bheki.service;

import com.enviro.assessment.junior.bheki.dto.WithdrawalNoticeRequest;
import com.enviro.assessment.junior.bheki.entity.Product;

public interface WithdrawalEligibility {

    boolean isEligible(Product product,double amount);
}
