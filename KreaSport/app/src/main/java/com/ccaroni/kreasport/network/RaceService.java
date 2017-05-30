package com.ccaroni.kreasport.network;

import com.ccaroni.kreasport.data.dto.Race;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Master on 23/04/2017.
 */

public interface RaceService {

    @GET("/races")
    Call<List<Race>> getPublicRaces();

}
