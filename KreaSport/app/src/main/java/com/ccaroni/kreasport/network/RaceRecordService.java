package com.ccaroni.kreasport.network;

import com.ccaroni.kreasport.data.dto.Race;
import com.ccaroni.kreasport.data.dto.RaceRecord;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Master on 30/05/2017.
 */

public interface RaceRecordService {

    @POST("/records")
    Call<Void> uploadRaceRecord(@Body RaceRecord raceRecord);

    @DELETE("/records/{id}")
    Call<RaceRecord> deleteRaceRecord(@Path("id") String recordId);

    @GET("/records/{id}")
    Call<RaceRecord> getRaceRecord(@Path("id") String id);
}
