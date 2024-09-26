package com.skillup.apiPresentation.dto;

import com.skillup.apiPresentation.dto.in.PromotionInDto;
import com.skillup.apiPresentation.dto.mapper.PromotionMapper;
import com.skillup.apiPresentation.dto.out.PromotionOutDto;
import com.skillup.apiPresentation.util.SkillUpCommon;
import com.skillup.application.promotion.PromotionApplication;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/promotion")
public class PromotionController {
    @Autowired
    PromotionService promotionService;

    @Autowired
    PromotionApplication promotionApplication;

    @PostMapping("")
    public PromotionOutDto createPromotion(@RequestBody PromotionInDto promotionInDto){
        PromotionDomain promotionDomain = promotionService.createPromotion(PromotionMapper.INSTANCE.toDomain(promotionInDto));
        return PromotionMapper.INSTANCE.toOutDto(promotionDomain);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<PromotionOutDto> getPromotionById(@PathVariable("id") String id){
        //1. get promotion cache domain

        //2. get stock cache

        //3. update stock onto available stock

        PromotionDomain promotionDomain = promotionApplication.getById(id);
        if(Objects.isNull(promotionDomain)){
            return ResponseEntity.status(SkillUpCommon.INTERNAL_ERROR).body(null);
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(PromotionMapper.INSTANCE.toOutDto(promotionDomain));
//        PromotionDomain promotionDomain = promotionService.getPromotionById(id);
//        if(Objects.isNull(promotionDomain)){
//            return ResponseEntity.status(SkillUpCommon.INTERNAL_ERROR).body(null);
//        }
//        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(PromotionMapper.INSTANCE.toOutDto(promotionDomain));
    }

    @GetMapping("/status/{status}")
    public List<PromotionOutDto> getByStatus(@PathVariable("status") Integer status){
        List<PromotionDomain> promotionDomainList = promotionService.getPromotionByStatus(status);
        return promotionDomainList.stream().map(promotionDomain -> PromotionMapper.INSTANCE.toOutDto(promotionDomain)).collect(Collectors.toList());
    }

    @PostMapping ("/lock/id/{id}")
    public ResponseEntity<Boolean> lockPromotionStock(@PathVariable("id") String id){
        // 1. check promotion exist
        PromotionDomain promotionDomain = promotionService.getPromotionById(id);
        if(Objects.isNull(promotionDomain)){
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(false);
        }
        // 2. try to lock stock
        boolean isLocked = promotionService.lockStock(id);
        if (isLocked){
            return ResponseEntity.status(SkillUpCommon.SUCCESS).body(true);
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(false);

    }

    @PostMapping("/revert/id/{id}")
    public ResponseEntity<Boolean> revertPromotionStock(@PathVariable("id") String id){
        // 1. check promotion exist
        PromotionDomain promotionDomain = promotionService.getPromotionById(id);
        if(Objects.isNull(promotionDomain)){
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(false);
        }
        // 2. try to lock stock
        boolean isRevert = promotionService.revertStock(id);
        if (isRevert){
            return ResponseEntity.status(SkillUpCommon.SUCCESS).body(true);
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(false);

    }
    @PostMapping("/deduct/id/{id}") // after payment, not longer lockF
    public ResponseEntity<Boolean> deductPromotionStock(@PathVariable("id") String id){
        // 1. check promotion exist
        PromotionDomain promotionDomain = promotionService.getPromotionById(id);
        if(Objects.isNull(promotionDomain)){
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(false);
        }
        // 2. try to deduct stock
        boolean isDeduct = promotionService.deductStock(id);
        if (isDeduct){
            return ResponseEntity.status(SkillUpCommon.SUCCESS).body(true);
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(false);

    }


//    private PromotionDomain toDomain(PromotionInDto inDto){
//        return PromotionDomain.builder()
//        .promotionId(UUID.randomUUID().toString())
//        .promotionName(inDto.getPromotionName())
//        .commodityId(inDto.getCommodityId())
//        .originalPrice(inDto.getOriginalPrice())
//        .promotionalPrice(inDto.getPromotionalPrice())
//        .startTime(inDto.getStartTime())
//        .endTime(inDto.getEndTime())
//        .status(inDto.getStatus())
//        .totalStock(inDto.getTotalStock())
//        .availableStock(inDto.getAvailableStock())
//        .lockStock(inDto.getLockStock())
//        .imageUrl(inDto.getImageUrl())
//        .build();
//    }
//
//    private PromotionOutDto toOutDto(PromotionDomain domain) {
//        return PromotionOutDto.builder()
//            .promotionId(domain.getPromotionId())
//            .promotionName(domain.getPromotionName())
//            .commodityId(domain.getCommodityId())
//            .originalPrice(domain.getOriginalPrice())
//            .promotionalPrice(domain.getPromotionalPrice())
//            .startTime(domain.getStartTime())
//            .endTime(domain.getEndTime())
//            .status(domain.getStatus())
//            .totalStock(domain.getTotalStock())
//            .availableStock(domain.getAvailableStock())
//            .lockStock(domain.getLockStock())
//            .imageUrl(domain.getImageUrl())
//            .build();
//  }
}
