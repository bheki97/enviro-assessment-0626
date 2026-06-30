package com.enviro.assessment.junior.bheki.service.impl;

import com.enviro.assessment.junior.bheki.dto.WithdrawalNoticeFilterDto;
import com.enviro.assessment.junior.bheki.entity.WithdrawalNotice;
import com.enviro.assessment.junior.bheki.exception.ApiException;
import com.enviro.assessment.junior.bheki.repository.WithdrawalNoticeRepository;
import com.enviro.assessment.junior.bheki.service.WithdrawalStatementsCSVGenerator;
import com.enviro.assessment.junior.bheki.util.WithdrawalNoticeFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WithdrawalStatementsCSVGeneratorImpl implements WithdrawalStatementsCSVGenerator {

    private final WithdrawalNoticeRepository withdrawalNoticeRepository;
    @Override
    public void generateWithdrawalStatementsCSV(WithdrawalNoticeFilterDto withdrawalNoticeFilterDto,
                                                HttpServletResponse response) {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=statements.csv");


        List<WithdrawalNotice> withdrawalNotices = withdrawalNoticeRepository
                .findAll(WithdrawalNoticeFilter.getWithdrawalSpecification(withdrawalNoticeFilterDto));
        try(PrintWriter writer = response.getWriter()){

            writer.println("AMOUNT,STATUS,CREATED DATE,PROCESSING DATE");
            StringBuilder builder = new StringBuilder();

            withdrawalNotices.forEach(withdrawalNotice -> {
                builder.append(withdrawalNotice.getAmount())
                .append(",")
                .append(withdrawalNotice.getStatus())
                .append(",")
                .append(withdrawalNotice.getCreatedDate())
                .append(",")
                .append(withdrawalNotice.getProcessedDate())
                .append("\n");
            });

            writer.println(builder);

        } catch (Exception e) {
            throw new ApiException("Could not Generate CSV File");
        }

    }
}
