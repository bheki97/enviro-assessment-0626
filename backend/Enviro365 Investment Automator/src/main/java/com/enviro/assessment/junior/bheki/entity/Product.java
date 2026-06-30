package com.enviro.assessment.junior.bheki.entity;


import com.enviro.assessment.junior.bheki.enumerate.ProductType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private InvestmentPortfolio investmentPortfolio;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<WithdrawalNotice> withdrawalNotices;
    private double balance;
    private ProductType productType;
    private LocalDateTime createdDate;

}
