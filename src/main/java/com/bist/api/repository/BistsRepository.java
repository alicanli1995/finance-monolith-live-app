package com.bist.api.repository;

import com.bist.api.model.Share;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BistsRepository extends MongoRepository<Share, String> {
}