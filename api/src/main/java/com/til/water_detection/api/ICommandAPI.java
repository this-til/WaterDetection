package com.til.water_detection.api;

import com.til.water_detection.data.Command;
import com.til.water_detection.data.Result;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ICommandAPI {
    @POST("/command/registerCommand")
    Call<Result<Void>> registerCommand(@Query("ruleId") int ruleId, @Query("actuatorId") int actuatorId, @Query("commandTrigger") int commandTrigger);

    @DELETE("/command/removeCommandById")
    Call<Result<Void>> removeCommandById(@Query("id") int id);

    @PUT("/command/updateCommandById")
    Call<Result<Void>> updateCommandById(@Query("id") int id, @Body Command command);

    @GET("/command/getCommandById")
    Call<Result<Command>> getCommandById(@Query("id") int id);

    @GET("/command/getCommandByIdArray")
    Call<Result<List<Command>>> getCommandByIdArray(@Query("id") int[] id);

    @GET("/command/getCommandByActuatorId")
    Call<Result<List<Command>>> getCommandByActuatorId(@Query("actuatorId") int actuatorId);

    @GET("/command/getCommandByRuleId")
    Call<Result<List<Command>>> getCommandByRuleId(@Query("ruleId") int ruleId);

    @GET("/command/getAllCommands")
    Call<Result<List<Command>>> getAllCommands();
}
