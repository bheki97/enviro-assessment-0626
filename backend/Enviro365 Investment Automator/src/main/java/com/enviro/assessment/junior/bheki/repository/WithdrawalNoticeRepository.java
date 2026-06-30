package com.enviro.assessment.junior.bheki.repository;

import com.enviro.assessment.junior.bheki.entity.WithdrawalNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WithdrawalNoticeRepository extends JpaRepository<WithdrawalNotice, String>,
        JpaSpecificationExecutor<WithdrawalNotice> {
}
