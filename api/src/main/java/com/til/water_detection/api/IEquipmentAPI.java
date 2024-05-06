package com.til.water_detection.api;

import com.til.water_detection.data.Equipment;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface IEquipmentAPI {
    @POST("/equipment/registerEquipment")
    Call<Result<Void>> registerEquipment(@Query("name") String name);

    @DELETE("/equipment/removeEquipmentPosById")
    Call<Result<Void>> removeEquipmentPosById(@Query("equipmentId") int equipmentId);

    @PUT("/equipment/updateEquipmentAnotherNameById")
    Call<Result<Void>> updateEquipmentAnotherNameById(@Query("id") int id, @Query("anotherName") String anotherName);

    @PUT("/equipment/updateEquipmentTimeById")
    Call<Result<Void>> updateEquipmentTimeById(@Query("id") int id);

    @GET("/equipment/getEquipmentById")
    Call<Result<Equipment>> getEquipmentById(@Query("id") int id);

    @GET("/equipment/getEquipmentByName")
    Call<Result<Equipment>> getEquipmentByName(@Query("name") String name);

    @GET("/equipment/getAllEquipment")
    Call<Result<List<Equipment>>> getAllEquipment();

    @GET("/equipment/getEquipmentByIdArray")
    Call<Result<List<Equipment>>> getEquipmentByIdArray(@Query("id") int[] id);

    @GET("/equipment/getEquipmentByNameArray")
    Call<Result<List<Equipment>>> getEquipmentByNameArray(@Query("name") String[] name);


}
