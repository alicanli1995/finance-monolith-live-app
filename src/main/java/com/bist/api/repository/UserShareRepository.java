package com.bist.api.repository;

import com.bist.api.model.UserShares;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserShareRepository extends MongoRepository<UserShares, String> {
}
