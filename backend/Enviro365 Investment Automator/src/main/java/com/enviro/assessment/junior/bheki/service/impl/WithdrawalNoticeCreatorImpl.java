package com.enviro.assessment.junior.bheki.service.impl;

import com.enviro.assessment.junior.bheki.dto.WithdrawalNoticeRequest;
import com.enviro.assessment.junior.bheki.dto.WithdrawalNoticeResponse;
import com.enviro.assessment.junior.bheki.entity.Product;
import com.enviro.assessment.junior.bheki.entity.WithdrawalNotice;
import com.enviro.assessment.junior.bheki.enumerate.WithdrawalStatus;
import com.enviro.assessment.junior.bheki.exception.ApiException;
import com.enviro.assessment.junior.bheki.mapper.WithdrawalNoticeMapper;
import com.enviro.assessment.junior.bheki.repository.ProductRepository;
import com.enviro.assessment.junior.bheki.repository.WithdrawalNoticeRepository;
import com.enviro.assessment.junior.bheki.service.WithdrawalEligibility;
import com.enviro.assessment.junior.bheki.service.WithdrawalNoticeCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class WithdrawalNoticeCreatorImpl implements WithdrawalNoticeCreator {

    private final WithdrawalEligibility withdrawalEligibility;
    private final WithdrawalNoticeRepository withdrawalNoticeRepository;
    private final ProductRepository productRepository;
    private final WithdrawalNoticeMapper withdrawalNoticeMapper;
    @Value("${log.msg.product}")
    private String logMsg;

    @Override
    public WithdrawalNoticeResponse createWithdrawalNotice(WithdrawalNoticeRequest request) {


        Product  product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ApiException("Invalid request"));

        if(!withdrawalEligibility.isEligible(product, request.getAmount())) {
            throw new ApiException("Withdrawal Notice not eligible");
        }

        WithdrawalNotice withdrawalNotice = new WithdrawalNotice();
        withdrawalNotice.setProduct(product);
        withdrawalNotice.setAmount(request.getAmount());
        withdrawalNotice.setStatus(WithdrawalStatus.PENDING);
        withdrawalNotice.setCreatedDate(LocalDateTime.now());

        withdrawalNoticeRepository.save(withdrawalNotice);

        return withdrawalNoticeMapper.toResponse(withdrawalNotice);
    }
}
