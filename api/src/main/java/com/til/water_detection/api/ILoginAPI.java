package com.til.water_detection.api;

import com.til.water_detection.data.Result;
import com.til.water_detection.data.Rule;
import com.til.water_detection.data.util.FinalString;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ILoginAPI {

    @POST("/login")
    Call<Result<String>> login(@Query(FinalString.USERNAME) String username, @Query(FinalString.PASSWORD) String password );

}
