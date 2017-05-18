package com.ccaroni.kreasport.network;

import com.ccaroni.kreasport.domain.User;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Master on 14/05/2017.
 */

public interface UserService {

    @GET("user")
    Call<User> getPrivateRaces();
}
