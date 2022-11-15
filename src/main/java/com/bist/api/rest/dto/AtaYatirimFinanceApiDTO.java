package com.bist.api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtaYatirimFinanceApiDTO{
    private String id;
    private String alert;
    private String message;
    private String success;
    private Result result;

}
