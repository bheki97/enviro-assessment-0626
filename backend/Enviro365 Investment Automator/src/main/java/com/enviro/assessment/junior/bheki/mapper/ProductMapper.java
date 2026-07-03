package com.enviro.assessment.junior.bheki.mapper;

import com.enviro.assessment.junior.bheki.dto.ProductResponse;
import com.enviro.assessment.junior.bheki.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = WithdrawalNoticeMapper.class)
public interface ProductMapper {

    ProductResponse toResponse(Product product);
}
