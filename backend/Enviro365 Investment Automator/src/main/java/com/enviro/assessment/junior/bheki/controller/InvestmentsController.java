package com.enviro.assessment.junior.bheki.controller;

import com.enviro.assessment.junior.bheki.dto.InvestmentPortfolioResponse;
import com.enviro.assessment.junior.bheki.dto.WithdrawalNoticeFilterDto;
import com.enviro.assessment.junior.bheki.dto.WithdrawalNoticeRequest;
import com.enviro.assessment.junior.bheki.dto.WithdrawalNoticeResponse;
import com.enviro.assessment.junior.bheki.enumerate.WithdrawalStatus;
import com.enviro.assessment.junior.bheki.service.InvestmentPortfolioRetriever;
import com.enviro.assessment.junior.bheki.service.WithdrawalNoticeCreator;
import com.enviro.assessment.junior.bheki.service.WithdrawalStatementsCSVGenerator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/investments/")
@RequiredArgsConstructor
public class InvestmentsController {

    private final InvestmentPortfolioRetriever investmentPortfolioRetriever;
    private final WithdrawalNoticeCreator  withdrawalNoticeCreator;
    private final WithdrawalStatementsCSVGenerator withdrawalStatementsCSVGenerator;


    @GetMapping("portfolio/{email}")
    public InvestmentPortfolioResponse getPortfolio(@PathVariable @NonNull String email) {
        return investmentPortfolioRetriever.retrieveInvestmentPortfolio(email);
    }

    @PostMapping("/withdrawal-notice")
    public WithdrawalNoticeResponse createWithdrawalNotice(@RequestBody WithdrawalNoticeRequest withdrawalNotice) {
        return withdrawalNoticeCreator.createWithdrawalNotice(withdrawalNotice);
    }

    @GetMapping("/withdrawal-notice/csv")
    public void generateWithdrawalNoticeCSV(
            @RequestParam @NonNull String productId,
            @RequestParam(required = false) WithdrawalStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            HttpServletResponse response) throws IOException {

        WithdrawalNoticeFilterDto dto = new WithdrawalNoticeFilterDto();
        dto.setProductId(productId);
        dto.setStatus(status);
        dto.setCreatedDate(fromDate);
        dto.setProcessedDate(toDate);
        log.info("Creating Withdrawal Notice CSV File");
        withdrawalStatementsCSVGenerator.generateWithdrawalStatementsCSV(dto,response);
    }


}
