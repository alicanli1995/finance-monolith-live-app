package com.bist.api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
@Document(collection = "user_finance_history")
public class UserFinanceHistory extends BaseEntity {

    @Id
    private String username;

    private List<History> history = new ArrayList<>();

    @Data
    @Builder
    @AllArgsConstructor
    public static class History {
        private LocalDateTime date;
        private BigDecimal totalDailyValue;
        private BigDecimal totalDailyChange;
        private BigDecimal totalDailyChangePercentage;
    }


}
