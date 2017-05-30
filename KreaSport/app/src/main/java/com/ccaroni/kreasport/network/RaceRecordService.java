package com.ccaroni.kreasport.network;

import com.ccaroni.kreasport.data.dto.RaceRecord;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Master on 30/05/2017.
 */

public interface RaceRecordService {

    @POST("/records")
    Call<RaceRecord> uploadRaceRecord(@Body RaceRecord raceRecord);
}
