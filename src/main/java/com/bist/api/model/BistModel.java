package com.bist.api.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@Document(collection = "bists_value")
public class BistModel {
    private String id;
    private String name;
    private String value;
    private LocalDateTime createdAt;
}
