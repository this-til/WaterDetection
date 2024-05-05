package com.til.water_detection.api;

import com.til.water_detection.data.Equipment;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface IEquipmentAPI {
    @POST("equipment/registerEquipment")
    Call<Result<Void>> registerEquipment(@Query("name") String name);

    @DELETE("equipment/removeEquipmentPosById")
    Call<Result<Void>> removeEquipmentPosById();

    @PUT("equipment/updateEquipmentAnotherNameById")
    Call<Result<Void>> updateEquipmentAnotherNameById(@Query("id") int id, @Query("anotherName") String anotherName);

    @PUT("equipment/updateEquipmentTimeById")
    Call<Result<Void>> updateEquipmentTimeById(@Query("id") int id);

    @GET("equipment/getEquipmentById")
    Call<Result<Equipment>> getEquipmentById(@Query("id") int id);

    @GET("equipment/getEquipmentByIdArray")
    Call<Result<List<Equipment>>> getEquipmentByIdArray(@Query("id") int[] id);

    @GET("equipment/getAllEquipment")
    Call<Result<List<Equipment>>> getAllEquipment();

}
