package com.bist.api.rest.dto;

import lombok.Builder;

@Builder
public record FinanceApiDTO(
        String dailyChangePercentage,
        String dailyChange,
        String c,
        String last,
        String dailyVolume,
        String previousDayClose,
        String description) {

}