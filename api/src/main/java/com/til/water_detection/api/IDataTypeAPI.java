package com.til.water_detection.api;

import com.til.water_detection.data.DataType;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

import java.util.List;

public interface IDataTypeAPI {

    @POST("dataType/register")
    Call<Result<Void>> register();

    @DELETE("dataType/removeDataTypeById")
    Call<Result<Void>> removeDataTypeById(int id);

    @GET("dataType/getAllDataType")
    Call<Result<List<DataType>>> getAllDataType();

    @GET("dataType/getDataTypeById")
    Call<Result<DataType>> getDataTypeById(int id);

    @GET("/getDataTypeByIdArray")
    Call<Result<List<DataType>>> getDataTypeByIdArray(int[] id);

    @PUT("dataType/updateDataTypeAnotherName")
    Call<Result<Void>> updateDataTypeAnotherName(int id, String anotherName);

}
