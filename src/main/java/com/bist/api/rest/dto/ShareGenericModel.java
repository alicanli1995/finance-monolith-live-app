package com.bist.api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShareGenericModel {
    private String name;
    private String value;
    private String dailyChangePercentage;
    private String dailyVolume;
    private String description;
    private String poster;
}
