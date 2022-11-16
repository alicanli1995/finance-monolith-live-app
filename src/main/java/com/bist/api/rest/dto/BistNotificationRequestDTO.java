package com.bist.api.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BistNotificationRequestDTO {

    private String bistName;
    private String value;
    private String mail;

}
