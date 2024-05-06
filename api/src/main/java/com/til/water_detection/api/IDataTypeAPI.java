package com.til.water_detection.api;

import com.til.water_detection.data.DataType;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface IDataTypeAPI {

    @POST("/dataType/register")
    Call<Result<Void>> registerDataType(@Query("name") String name);

    @DELETE("/dataType/removeDataTypeById")
    Call<Result<Void>> removeDataTypeById(@Query("id") int id);

    @GET("/dataType/getDataTypeById")
    Call<Result<DataType>> getDataTypeById(@Query("id") int id);

    @GET("/dataType/getDataTypeByName")
    Call<Result<DataType>> getDataTypeByName(@Query("name") String name);

    @GET("/dataType/getAllDataType")
    Call<Result<List<DataType>>> getAllDataType();

    @GET("/dataType/getDataTypeByIdArray")
    Call<Result<List<DataType>>> getDataTypeByIdArray(@Query("id") int[] id);

    @GET("/dataType/getDataTypeByNameArray")
    Call<Result<List<DataType>>> getDataTypeByNameArray(@Query("name") String[] name);

}
