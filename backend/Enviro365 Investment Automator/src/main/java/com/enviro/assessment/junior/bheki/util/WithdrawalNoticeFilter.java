package com.enviro.assessment.junior.bheki.util;

import com.enviro.assessment.junior.bheki.dto.WithdrawalNoticeFilterDto;
import com.enviro.assessment.junior.bheki.entity.Product;
import com.enviro.assessment.junior.bheki.entity.WithdrawalNotice;
import com.enviro.assessment.junior.bheki.enumerate.WithdrawalStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WithdrawalNoticeFilter {

    public static Specification<WithdrawalNotice> getWithdrawalSpecification(WithdrawalNoticeFilterDto dto) {
        return (root,query,cb)->{
            List<Predicate> predicates = new ArrayList<>();
            Join<WithdrawalNotice, Product> product = root.join("product");

            predicates.add(cb.like(product.get("id") ,dto.getProductId()));
            WithdrawalStatus status = dto.getStatus();
            if(status != null){
                predicates.add(cb.equal(root.get("status"), status));
            }
            LocalDateTime date = dto.getCreatedDate();
            if(date != null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), date));
            }
            date = dto.getProcessedDate();
            if(date != null){
                predicates.add(cb.lessThanOrEqualTo(root.get("processedDate"), date));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }
}
