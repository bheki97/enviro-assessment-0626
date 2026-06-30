package com.enviro.assessment.junior.bheki.mapper;

import com.enviro.assessment.junior.bheki.dto.WithdrawalNoticeResponse;
import com.enviro.assessment.junior.bheki.entity.WithdrawalNotice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WithdrawalNoticeMapper {

    WithdrawalNoticeResponse toResponse(WithdrawalNotice withdrawalNotice);
}
