package com.enviro.assessment.junior.bheki.service;

import com.enviro.assessment.junior.bheki.dto.WithdrawalNoticeFilterDto;
import com.enviro.assessment.junior.bheki.entity.WithdrawalNotice;
import jakarta.servlet.http.HttpServletResponse;

public interface WithdrawalStatementsCSVGenerator {

    void generateWithdrawalStatementsCSV(WithdrawalNoticeFilterDto withdrawalNoticeFilterDto, HttpServletResponse response);
}
