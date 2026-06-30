package com.enviro.assessment.junior.bheki.dto;

import com.enviro.assessment.junior.bheki.enumerate.WithdrawalStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WithdrawalNoticeFilterDto {

    @NotBlank
    private String productId;
    private WithdrawalStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime processedDate;
}
