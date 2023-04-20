package com.skillup.apiPresentation.dto.mapper;

import com.skillup.apiPresentation.dto.in.PromotionInDto;
import com.skillup.apiPresentation.dto.out.PromotionOutDto;
import com.skillup.domain.promotion.PromotionDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PromotionMapper {
    // 创建一个父类引用，生成一个用mapStruct调用方法的实例，同时完成下面用接口写出的方法，根据方法中的参数来做map。
    PromotionMapper INSTANCE = Mappers.getMapper(PromotionMapper.class);
    @Mapping(source = "promotionName", target = "promotionName")
    PromotionDomain toDomain(PromotionInDto inDto);

    PromotionOutDto toOutDto(PromotionDomain promotionDomain);
}
