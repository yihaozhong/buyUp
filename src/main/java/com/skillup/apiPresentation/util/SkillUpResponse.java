package com.skillup.apiPresentation.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillUpResponse {
    // json format

    Object result;
    String msg;
}
