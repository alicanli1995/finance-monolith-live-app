package com.bist.api.repository;

import com.bist.api.model.BistModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BistValueRepository extends MongoRepository<BistModel,String > {
    List<BistModel> findAllByName(String name);
}
