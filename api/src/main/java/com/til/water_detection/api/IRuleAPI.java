package com.til.water_detection.api;

import com.til.water_detection.data.Result;
import com.til.water_detection.data.Rule;
import com.til.water_detection.data.util.FinalString;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface IRuleAPI {
    @POST("/rule/registerRule")
    Call<Result<Void>> registerRule(@Header(FinalString.TOKEN) String token, @Body Rule rule);

    @DELETE("/rule/deleteByID")
    Call<Result<Void>> deleteByID(@Header(FinalString.TOKEN) String token, @Query("id") int id);

    @PUT("/rule/updateById")
    Call<Result<Void>> updateById(@Header(FinalString.TOKEN) String token, @Query("id") int id, @Body Rule rule);

    @GET("/rule/getRuleById")
    Call<Result<Rule>> getRuleById(@Header(FinalString.TOKEN) String token, @Query("id") int id);

    @GET("/rule/getRuleByEquipmentId")
    Call<Result<List<Rule>>> getRuleByEquipmentId(@Header(FinalString.TOKEN) String token, @Query("equipmentId") int equipmentId);

    @GET("/rule/getRuleByDataTypeId")
    Call<Result<List<Rule>>> getRuleByDataTypeId(@Header(FinalString.TOKEN) String token, @Query("dataTypeId") int dataTypeId);

    @GET("/rule/getRuleByEquipmentIdArray")
    Call<Result<List<Rule>>> getRuleByEquipmentIdArray(@Header(FinalString.TOKEN) String token, @Query("id") int[] id);

    @GET("/rule/getAllRule")
    Call<Result<List<Rule>>> getAllRule(@Header(FinalString.TOKEN) String token);
}
