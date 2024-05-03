package com.til.water_detection.api;

import com.til.water_detection.data.DataType;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface IDataTypeAPI {

    @POST("dataType/register")
    Call<Result<Void>> register();

    @DELETE("dataType/removeDataTypeById")
    Call<Result<Void>> removeDataTypeById(@Query("id") int id);

    @GET("dataType/getAllDataType")
    Call<Result<List<DataType>>> getAllDataType();

    @GET("dataType/getDataTypeById")
    Call<Result<DataType>> getDataTypeById(@Query("id") int id);

    @GET("dataType/getDataTypeByIdArray")
    Call<Result<List<DataType>>> getDataTypeByIdArray(@Body int[] id);

    @PUT("dataType/updateDataTypeAnotherName")
    Call<Result<Void>> updateDataTypeAnotherName(@Query("id") int id, @Query("id") String anotherName);

}
