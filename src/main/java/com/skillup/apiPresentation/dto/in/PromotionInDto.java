package com.skillup.apiPresentation.dto.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionInDto {
    private String promotionName;
    private String commodityId;
    private Integer originalPrice;
    private Integer promotionalPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer status;

    private Long totalStock;

    private Long availableStock;

    private Long lockStock;
    private String imageUrl;
}
