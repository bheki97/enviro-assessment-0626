package com.enviro.assessment.junior.bheki.service;


import com.enviro.assessment.junior.bheki.dto.WithdrawalNoticeRequest;
import com.enviro.assessment.junior.bheki.dto.WithdrawalNoticeResponse;

public interface WithdrawalNoticeCreator {

    WithdrawalNoticeResponse createWithdrawalNotice(WithdrawalNoticeRequest request);
}
