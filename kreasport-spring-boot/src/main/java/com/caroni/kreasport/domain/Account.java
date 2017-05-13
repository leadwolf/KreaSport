package com.caroni.kreasport.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Master on 12/05/2017.
 */
@Document(collection = "accounts")
public class Account {

    @Id
    private String id;

    private String username;
    private String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.id = new ObjectId().toString();
    }

    public Account() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
