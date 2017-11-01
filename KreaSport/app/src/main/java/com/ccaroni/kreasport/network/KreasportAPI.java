package com.ccaroni.kreasport.network;

import com.ccaroni.kreasport.data.legacy.dto.Race;
import com.ccaroni.kreasport.data.legacy.dto.RaceRecord;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Master on 30/05/2017.
 */

public interface KreasportAPI {

    @POST("/records")
    Call<Void> uploadRaceRecord(@Body RaceRecord raceRecord);

    @DELETE("/records/{id}")
    Call<Void> deleteRaceRecord(@Path("id") String recordId);

    @GET("/records/{id}")
    Call<RaceRecord> getRaceRecord(@Path("id") String id);

    @DELETE("/records/batch")
    Call<Void> deleteMultipleRecords(@Body List<String> idsToDeleteList);

    @POST("/records/batch")
    Call<Void> uploadMultipleRaceRecords(@Body List<RaceRecord> raceRecords);

    @GET("/races")
    Call<List<Race>> getPublicRaces();
}
