package com.til.water_detection.api;

import com.til.water_detection.data.Equipment;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.util.FinalString;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface IEquipmentAPI {
    @POST("/equipment/registerEquipment")
    Call<Result<Void>> registerEquipment(@Header(FinalString.TOKEN) String token, @Query("name") String name);

    @DELETE("/equipment/removeEquipmentPosById")
    Call<Result<Void>> removeEquipmentPosById(@Header(FinalString.TOKEN) String token, @Query("equipmentId") int equipmentId);

    @PUT("/equipment/updateEquipmentAnotherNameById")
    Call<Result<Void>> updateEquipmentAnotherNameById(@Header(FinalString.TOKEN) String token, @Query("id") int id, @Query("anotherName") String anotherName);

    @PUT("/equipment/updateEquipmentTimeById")
    Call<Result<Void>> updateEquipmentTimeById(@Header(FinalString.TOKEN) String token, @Query("id") int id);

    @GET("/equipment/getEquipmentById")
    Call<Result<Equipment>> getEquipmentById(@Header(FinalString.TOKEN) String token, @Query("id") int id);

    @GET("/equipment/getEquipmentByName")
    Call<Result<Equipment>> getEquipmentByName(@Header(FinalString.TOKEN) String token, @Query("name") String name);

    @GET("/equipment/getAllEquipment")
    Call<Result<List<Equipment>>> getAllEquipment(@Header(FinalString.TOKEN) String token);

    @GET("/equipment/getEquipmentByIdArray")
    Call<Result<List<Equipment>>> getEquipmentByIdArray(@Header(FinalString.TOKEN) String token, @Query("id") int[] id);

    @GET("/equipment/getEquipmentByNameArray")
    Call<Result<List<Equipment>>> getEquipmentByNameArray(@Header(FinalString.TOKEN) String token, @Query("name") String[] name);


}
