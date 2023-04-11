package com.skillup.apiPresentation.dto.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInDto {
    @NotBlank(message = "promotion ID cannot be empty")
    private String promotionId;

    private String promotionName;

    private String userId;

    private Integer orderAmount;

}
