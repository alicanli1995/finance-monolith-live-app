package com.bist.api.repository;

import com.bist.api.model.UserFavShares;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserFavRepository extends MongoRepository<UserFavShares,String> {
    Optional<UserFavShares> findByUsername(String username);
}
