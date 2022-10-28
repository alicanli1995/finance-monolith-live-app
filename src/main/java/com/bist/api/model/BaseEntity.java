package com.bist.api.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Getter
@Setter
@ToString
public abstract class BaseEntity{

    @CreatedBy
    @JsonIgnore
    @Field("created_by")
    private String createdBy = "system";

    @JsonIgnore
    @CreatedDate
    @Field("created_date")
    private Instant createdDate = Instant.now();

    @JsonIgnore
    @LastModifiedBy
    @Field(value = "updatedBy")
    private String lastModifiedBy;

    @JsonIgnore
    @LastModifiedDate
    @Field(value = "updatedDate")
    private Instant lastModifiedDate = Instant.now();

}
