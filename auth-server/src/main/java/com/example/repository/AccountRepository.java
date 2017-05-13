package com.example.repository;

import com.example.domain.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Master on 10/05/2017.
 */
public interface AccountRepository extends MongoRepository<Account, String> {

    public Account findByUsername(String username);

}