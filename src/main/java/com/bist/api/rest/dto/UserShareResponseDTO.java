package com.bist.api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserShareResponseDTO {
    private String name;
    private String amount;
    private String poster;
    private String totalAmount;
    private String price;
}
