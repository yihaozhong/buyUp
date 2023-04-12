package com.skillup.apiPresentation.dto;

import com.skillup.apiPresentation.dto.in.CommodityInDto;
import com.skillup.apiPresentation.dto.out.CommodityOutDto;
import com.skillup.apiPresentation.util.SkillUpCommon;
import com.skillup.apiPresentation.util.SkillUpResponse;
import com.skillup.domain.commodity.CommodityDomain;
import com.skillup.domain.commodity.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/commodity")
public class CommodityController {
    @Autowired
    CommodityService commodityService;
    @PostMapping("")
    public ResponseEntity<SkillUpResponse> createCommodity(@RequestBody CommodityInDto commodityInDto){

        CommodityDomain commodityDomain;
        // insert data into data table
        try{
            commodityDomain = commodityService.createCommodity(toDomain(commodityInDto));
            return ResponseEntity.status(SkillUpCommon.SUCCESS).body(SkillUpResponse.builder()
                    .msg(null)
                    .result(toOutDto(commodityDomain)).build());
        } catch (Exception e){
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(SkillUpResponse.builder()
                    .msg(String.format(SkillUpCommon.COMMODITY_EXISTS, commodityInDto.getCommodityName()))
                    .result(null).build());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<SkillUpResponse> readAccountById(@PathVariable("id") String id){
        CommodityDomain commodityDomain = commodityService.readCommodityById(id);
        //
        if (Objects.isNull(commodityDomain)){
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(
                    SkillUpResponse.builder()
                            .msg(String.format(SkillUpCommon.COMMODITY_ID_WRONG, id))
                            .result(null).build()
            );
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(
                SkillUpResponse.builder()
                        .result(toOutDto(commodityDomain)).build());
    }
    


    private CommodityDomain toDomain(CommodityInDto commodityInDto){
        return CommodityDomain.builder().commodityId(UUID.randomUUID().toString())
                .commodityName(commodityInDto.getCommodityName())
                .description(commodityInDto.getDescription())
                .price(commodityInDto.getPrice())
                .imageUrl(commodityInDto.getImageUrl()).build();
    }

    private CommodityOutDto toOutDto(CommodityDomain commodityDomain){
        return CommodityOutDto.builder().commodityId(commodityDomain.getCommodityId())
                .commodityName(commodityDomain.getCommodityName())
                .description(commodityDomain.getDescription())
                .price(commodityDomain.getPrice())
                .imageUrl(commodityDomain.getImageUrl()).build();
    }
}
