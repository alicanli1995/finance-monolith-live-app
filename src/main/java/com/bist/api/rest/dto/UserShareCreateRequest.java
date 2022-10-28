package com.bist.api.rest.dto;

import lombok.Builder;

@Builder
public record UserShareCreateRequest(
    String name,
    String amount) {}
