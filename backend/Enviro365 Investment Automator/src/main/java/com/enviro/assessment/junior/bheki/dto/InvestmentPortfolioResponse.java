package com.enviro.assessment.junior.bheki.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InvestmentPortfolioResponse {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private int numberOfInvestments;
    private LocalDateTime createdDate;
    private List<ProductResponse> products;
}
