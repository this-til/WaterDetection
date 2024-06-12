package com.til.water_detection.api;

import com.til.water_detection.data.Actuator;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.util.FinalString;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface IActuatorAPI {
    @POST("/actuator/registerActuator")
    Call<Result<Void>> registerActuator(@Header(FinalString.TOKEN) String token, @Query("name") String name);

    @DELETE("/actuator/removeActuatorById")
    Call<Result<Void>> removeActuatorById(@Header(FinalString.TOKEN) String token, @Query("id") int id);

    @GET("/actuator/getActuatorById")
    Call<Result<Actuator>> getActuatorById(@Header(FinalString.TOKEN) String token, @Query("id") int id);

    @GET("/actuator/getActuatorByName")
    Call<Result<Actuator>> getActuatorByName(@Header(FinalString.TOKEN) String token, @Query("name") String name);

    @GET("/actuator/getAllActuator")
    Call<Result<List<Actuator>>> getAllActuator(@Header(FinalString.TOKEN) String token);

    @GET("/actuator/getActuatorByIdArray")
    Call<Result<List<Actuator>>> getActuatorByIdArray(@Header(FinalString.TOKEN) String token, @Query("id") int[] id);

    @GET("/actuator/getActuatorByNameArray")
    Call<Result<List<Actuator>>> getActuatorByNameArray(@Header(FinalString.TOKEN) String token, @Query("name") String[] name);
}
