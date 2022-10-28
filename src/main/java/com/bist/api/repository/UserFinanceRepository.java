package com.bist.api.repository;

import com.bist.api.model.UserFinanceHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFinanceRepository extends MongoRepository<UserFinanceHistory, String> {
}
