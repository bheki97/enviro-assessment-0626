package com.enviro.assessment.junior.bheki.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class InvestmentPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private int age;
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "investmentPortfolio",fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Product> products;

}
