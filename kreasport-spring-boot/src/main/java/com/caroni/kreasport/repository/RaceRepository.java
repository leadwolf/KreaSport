package com.caroni.kreasport.repository;

import com.caroni.kreasport.domain.Race;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Master on 12/05/2017.
 */
@Repository
public interface RaceRepository extends MongoRepository<Race, String> {

    Race findById(String id);

    Race findFirstByTitle(String title);

    List<Race> findByTitle(String title);

    List<Race> deleteByTitle(String title);

    Race deleteById(String id);
}
