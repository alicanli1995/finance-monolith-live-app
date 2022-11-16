package com.bist.api.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
@Document(collection = "bists_notifications")
public class BistNotification extends BaseEntity {

        @Id
        private String id;
        private String bistName;
        private String mailAddress;
        private String value;
        private boolean isSent;


}
