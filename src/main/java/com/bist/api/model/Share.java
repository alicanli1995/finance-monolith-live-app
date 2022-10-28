package com.bist.api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
@Document(collection = "bists")
public class Share extends BaseEntity {

    @Id
    private String name;
    private String value;
    private String dailyChangePercentage;
    private String dailyVolume;
    private String previousDayClose;
    private String description;
    private String dailyChange;
    private String poster;
    private List<Comment> comments = new ArrayList<>();

    @Data
    @AllArgsConstructor
    public static class Comment {
        private String username;
        private String text;
        private LocalDateTime timestamp;
    }

}
