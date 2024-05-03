package com.til.water_detection.api;

import com.til.water_detection.data.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IDataAPI {

    @POST("data/addData")
    Call<Result<Void>> addData(@Body Data data);


    @POST("data/addDataSimple")
    Call<Result<Void>> addDataSimple(
            @Query("equipmentId") int equipmentId,
            @Query("dataTypeId") int dataTypeId,
            @Query("value") float value);

    @POST("data/addDataList")
    Call<Result<Void>> addDataList(@Body List<Data> dataList);

    @POST("data/getAllData")
    Call<Result<List<Data>>> getAllData();

    @GET("data/getData")
    Call<Result<List<Data>>> getData(
            @Query("equipmentId") int equipmentId,
            @Query("dataTypeId") int dataTypeId,
            @Query("start") Timestamp start,
            @Query("end") Timestamp end
    );

    @POST("data/getDataToDataSheet")
    Call<Result<DataSheet>> getDataToDataSheet(@Body DataFilter dataFilter);
}
