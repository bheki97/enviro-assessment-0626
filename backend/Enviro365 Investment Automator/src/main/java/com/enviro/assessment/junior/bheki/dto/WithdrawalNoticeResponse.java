package com.enviro.assessment.junior.bheki.dto;

import com.enviro.assessment.junior.bheki.enumerate.WithdrawalStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WithdrawalNoticeResponse {

    private String id;
    private double amount;
    private WithdrawalStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime processedDate;
}
