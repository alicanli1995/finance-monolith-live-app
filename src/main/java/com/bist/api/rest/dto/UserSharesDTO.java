package com.bist.api.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserSharesDTO {

    private String name;

    private String value;

    private String amount;

    private String totalAmount;


}
