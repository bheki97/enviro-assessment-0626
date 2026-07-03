package com.enviro.assessment.junior.bheki.entity;


import com.enviro.assessment.junior.bheki.enumerate.WithdrawalStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class WithdrawalNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private double amount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Product product;
    private WithdrawalStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime processedDate;
}
