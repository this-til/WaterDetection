package com.til.water_detection.api;

import com.til.water_detection.data.Equipment;
import com.til.water_detection.data.Result;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

import java.util.List;

public interface IEquipmentAPI {
    @POST("equipment/register")
    Call<Result<Void>> register();

    @DELETE("equipment/removeEquipmentPosById")
    Call<Result<Void>> removeEquipmentPosById();

    @GET("equipment/getAllEquipment")
    Call<Result<List<Equipment>>> getAllEquipment();

    @GET("equipment/getEquipmentById")
    Call<Result<Equipment>> getEquipmentById(int id);

    @PUT("equipment/updateEquipmentAnotherNameById")
    Call<Result<Void>> updateEquipmentAnotherNameById(int id, String anotherName);
}
