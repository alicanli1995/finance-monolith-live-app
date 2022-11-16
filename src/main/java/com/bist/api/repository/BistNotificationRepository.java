package com.bist.api.repository;

import com.bist.api.model.BistNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BistNotificationRepository extends MongoRepository<BistNotification, String> {
    Optional<BistNotification> findByBistNameAndMailAddress(String bistName, String mailAddress);

}