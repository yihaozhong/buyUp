package com.skillup.apiPresentation.dto.mapper;

import com.skillup.apiPresentation.dto.in.PromotionInDto;
import com.skillup.apiPresentation.dto.out.PromotionOutDto;
import com.skillup.domain.promotion.PromotionDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PromotionMapper {
    PromotionMapper INSTANCE = Mappers.getMapper(PromotionMapper.class);
    @Mapping(source = "promotionName", target = "promotionName")
    PromotionDomain toDomain(PromotionInDto inDto);

    PromotionOutDto toOutDto(PromotionDomain promotionDomain);
}
