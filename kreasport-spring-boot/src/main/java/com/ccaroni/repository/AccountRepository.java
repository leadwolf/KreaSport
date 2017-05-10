package com.ccaroni.repository;

import com.ccaroni.domain.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Master on 10/05/2017.
 */
public interface AccountRepository extends MongoRepository<Account, String> {

    public Account findByUsername(String username);

}