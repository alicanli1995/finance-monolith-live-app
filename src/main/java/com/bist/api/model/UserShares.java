package com.bist.api.model;

import com.bist.api.rest.dto.UserSharesDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
@Document(collection = "user_shares")
public class UserShares extends BaseEntity {

    @Id
    private String username;

    private List<UserSharesDTO> shares;

    private BigDecimal totalValue;

}
