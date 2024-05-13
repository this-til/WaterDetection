package com.til.water_detection.api;

import com.til.water_detection.data.Result;
import com.til.water_detection.data.Rule;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface IRuleAPI {
    @POST("/rule/registerRule")
    Call<Result<Void>> registerRule(@Body Rule rule);

    @DELETE("/rule/deleteByID")
    Call<Result<Void>> deleteByID(@Query("id") int id);

    @PUT("/rule/updateById")
    Call<Result<Void>> updateById(@Query("id") int id, @Body Rule rule);

    @GET("/rule/getRuleById")
    Call<Result<Rule>> getRuleById(@Query("id") int id);

    @GET("/rule/getRuleByEquipmentId")
    Call<Result<List<Rule>>> getRuleByEquipmentId(@Query("equipmentId") int equipmentId);

    @GET("/rule/getRuleByDataTypeId")
    Call<Result<List<Rule>>> getRuleByDataTypeId(@Query("dataTypeId") int dataTypeId);

    @GET("/rule/getRuleByEquipmentIdArray")
    Call<Result<List<Rule>>> getRuleByEquipmentIdArray(@Query("id") int[] id);

    @GET("/rule/getAllRule")
    Call<Result<List<Rule>>> getAllRule();
}
