package com.til.water_detection.api;

import com.til.water_detection.data.Actuator;
import com.til.water_detection.data.Command;
import com.til.water_detection.data.Result;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface IActuatorAPI {
    @POST("/actuator/registerActuator")
    Call<Result<Void>> registerActuator(@Query("name") String name);

    @DELETE("/actuator/removeActuatorById")
    Call<Result<Void>> removeActuatorById(@Query("id") int id);

    @GET("/actuator/getActuatorById")
    Call<Result<Actuator>> getActuatorById(@Query("id") int id);

    @GET("/actuator/getActuatorByName")
    Call<Result<Actuator>> getActuatorByName(@Query("name") String name);

    @GET("/actuator/getAllActuator")
    Call<Result<List<Actuator>>> getAllActuator();

    @GET("/actuator/getActuatorByIdArray")
    Call<Result<List<Actuator>>> getActuatorByIdArray(@Query("id") int[] id);

    @GET("/actuator/getActuatorByNameArray")
    Call<Result<List<Actuator>>> getActuatorByNameArray(@Query("name") String[] name);
}
