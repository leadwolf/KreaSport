package com.ccaroni.kreasport;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by Master on 16/04/2017.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.ccaroni.kreasport.repository")
public class MongoConfig extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "kreasport-mongodb";
    }

    @Override
    public MongoClient mongoClient() {
        String MONGO_URI = System.getenv("MONGO_MLAB");
        MongoClientURI uri = new MongoClientURI(MONGO_URI);
        return new MongoClient(uri);
    }

}