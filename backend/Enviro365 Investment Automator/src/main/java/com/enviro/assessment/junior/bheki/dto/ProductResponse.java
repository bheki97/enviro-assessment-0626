package com.enviro.assessment.junior.bheki.dto;


import com.enviro.assessment.junior.bheki.enumerate.ProductType;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {


    private String id;
    private List<WithdrawalNoticeResponse> withdrawalNotices;
    private double balance;
    private ProductType productType;
}
