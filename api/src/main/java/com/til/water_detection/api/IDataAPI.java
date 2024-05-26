package com.til.water_detection.api;

import com.til.water_detection.data.*;
import com.til.water_detection.data.util.FinalString;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IDataAPI {

    @POST("/data/addData")
    Call<Result<Void>> addData(@Header(FinalString.TOKEN) String token, @Body Data data);

    @POST("/data/addDataSimple")
    Call<Result<Void>> addDataSimple(
            @Header(FinalString.TOKEN) String token,
            @Query("equipmentId") int equipmentId,
            @Query("dataTypeId") int dataTypeId,
            @Query("time") @Nullable Timestamp time,
            @Query("value") float value
    );

    @POST("/data/addDataList")
    Call<Result<Void>> addDataList(@Header(FinalString.TOKEN) String token, @Body List<Data> dataList);

    @GET("/data/getDataById")
    Call<Result<Data>> getDataById(@Header(FinalString.TOKEN) String token, @Query("id") long id);

    @GET("/data/getAllData")
    Call<Result<List<Data>>> getAllData(@Header(FinalString.TOKEN) String token);

    @GET("/data/getData")
    Call<Result<List<Data>>> getData(
            @Header(FinalString.TOKEN) String token,
            @Query("equipmentId") int equipmentId,
            @Query("dataTypeId") int dataTypeId,
            @Query("start") Timestamp start,
            @Query("end") Timestamp end
    );

    @GET("/data/getDataToDataSheet")
    Call<Result<DataSheet>> getDataToDataSheet(
            @Header(FinalString.TOKEN) String token,
            @Query("dataTypeId") int dataTypeId,
            @Query("equipmentIdArray") int[] equipmentIdArray,
            @Query("timeStep") int timeStep,
            @Query("startTime") Timestamp startTime,
            @Query("endTime") Timestamp endTime
    );

}
