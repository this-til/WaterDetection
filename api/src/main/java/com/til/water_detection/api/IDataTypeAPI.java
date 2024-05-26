package com.til.water_detection.api;

import com.til.water_detection.data.DataType;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.util.FinalString;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface IDataTypeAPI {

    @POST("/dataType/register")
    Call<Result<Void>> registerDataType(@Header(FinalString.TOKEN) String token, @Query("name") String name);

    @DELETE("/dataType/removeDataTypeById")
    Call<Result<Void>> removeDataTypeById(@Header(FinalString.TOKEN) String token, @Query("id") int id);

    @GET("/dataType/getDataTypeById")
    Call<Result<DataType>> getDataTypeById(@Header(FinalString.TOKEN) String token, @Query("id") int id);

    @GET("/dataType/getDataTypeByName")
    Call<Result<DataType>> getDataTypeByName(@Header(FinalString.TOKEN) String token, @Query("name") String name);

    @GET("/dataType/getAllDataType")
    Call<Result<List<DataType>>> getAllDataType(@Header(FinalString.TOKEN) String token );

    @GET("/dataType/getDataTypeByIdArray")
    Call<Result<List<DataType>>> getDataTypeByIdArray(@Header(FinalString.TOKEN) String token, @Query("id") int[] id);

    @GET("/dataType/getDataTypeByNameArray")
    Call<Result<List<DataType>>> getDataTypeByNameArray(@Header(FinalString.TOKEN) String token, @Query("name") String[] name);

}
