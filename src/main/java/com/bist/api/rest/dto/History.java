package com.bist.api.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record History(
        BigDecimal value,
        Instant date
) {
}
