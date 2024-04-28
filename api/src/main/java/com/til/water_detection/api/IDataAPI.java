package com.til.water_detection.api;

import com.til.water_detection.data.Data;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface IDataAPI {

    @POST("data/addData")
    Call<Result<Void>> addData(Data data);

    @GET("data/getDataById")
    Call<Result<Data>> getDataById(long id);

    @GET("data/getAllData")
    Call<Result<List<Data>>> getAllData();

    @GET("data/getData")
    Call<Result<List<Data>>> getData(
            int equipmentId,
            int dataTypeId,
            long start,
            long end
    );
}
