package com.bist.api.model;

import com.bist.api.rest.dto.UserFavDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@Document(collection = "user_fav_shares")
public class UserFavShares {
    @Id
    private String username;
    private List<UserFavDTO> shares;

}
