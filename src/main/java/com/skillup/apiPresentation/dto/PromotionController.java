package com.skillup.apiPresentation.dto;

import com.skillup.apiPresentation.dto.in.PromotionInDto;
import com.skillup.apiPresentation.dto.out.PromotionOutDto;
import com.skillup.apiPresentation.util.SkillUpCommon;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/promotion")
public class PromotionController {
    @Autowired
    PromotionService promotionService;
    @PostMapping("")
    public PromotionOutDto createPromotion(@RequestBody PromotionInDto promotionInDto){
        PromotionDomain promotionDomain = promotionService.createPromotion(toDomain(promotionInDto));
        return toOutDto(promotionDomain);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<PromotionOutDto> getPromotionById(@PathVariable("id") String id){
        PromotionDomain promotionDomain = promotionService.getPromotionById(id);
        if(Objects.isNull(promotionDomain)){
            return ResponseEntity.status(SkillUpCommon.INTERNAL_ERROR).body(null);
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(toOutDto(promotionDomain));
    }

    private PromotionDomain toDomain(PromotionInDto inDto){
        return PromotionDomain.builder()
        .promotionId(UUID.randomUUID().toString())
        .promotionName(inDto.getPromotionName())
        .commodityId(inDto.getCommodityId())
        .originalPrice(inDto.getOriginalPrice())
        .promotionalPrice(inDto.getPromotionalPrice())
        .startTime(inDto.getStartTime())
        .endTime(inDto.getEndTime())
        .status(inDto.getStatus())
        .totalStock(inDto.getTotalStock())
        .availableStock(inDto.getAvailableStock())
        .lockStock(inDto.getLockStock())
        .imageUrl(inDto.getImageUrl())
        .build();
    }

    private PromotionOutDto toOutDto(PromotionDomain domain) {
        return PromotionOutDto.builder()
            .promotionId(domain.getPromotionId())
            .promotionName(domain.getPromotionName())
            .commodityId(domain.getCommodityId())
            .originalPrice(domain.getOriginalPrice())
            .promotionalPrice(domain.getPromotionalPrice())
            .startTime(domain.getStartTime())
            .endTime(domain.getEndTime())
            .status(domain.getStatus())
            .totalStock(domain.getTotalStock())
            .availableStock(domain.getAvailableStock())
            .lockStock(domain.getLockStock())
            .imageUrl(domain.getImageUrl())
            .build();
  }
}
