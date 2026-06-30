package com.enviro.assessment.junior.bheki.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WithdrawalNoticeRequest {

    @NotBlank
    private String productId;
    @NotBlank
    private double amount;


}
