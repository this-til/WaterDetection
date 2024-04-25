package com.til.water_detection.api;

import com.til.water_detection.data.Detection;
import com.til.water_detection.data.Result;
import com.til.water_detection.data.ResultType;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

import java.util.List;

public interface IDetectionAPI {
    @POST("detection/register")
    Call<Result<Void>> register();

    @GET("detection/getAllDetections")
    Call<Result<List<Detection>>> getAllDetections();

    @GET("detection/getDetectionById")
    Call<Result<Detection>> getDetectionById(int id);

    @PUT("detection/updateDetectionAnotherNameById")
    Call<Result<Void>> updateDetectionAnotherNameById(int id, String anotherName);
}
