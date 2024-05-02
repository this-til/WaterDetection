package com.til.water_detection.api;

import com.til.water_detection.data.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Timestamp start,
            Timestamp end
    );

    @GET("data/getDataMapFromEquipmentIdArray")
    Call<Result<Map<Integer, List<Data>>>> getDataMapFromEquipmentIdArray(
            int[] equipmentIdArray,
            int dataTypeId,
            Timestamp start,
            Timestamp end
    );

    @GET("data/getDataMapFromDataTypeIdArray")
    Call<Result<Map<Integer, List<Data>>>> getDataMapFromDataTypeIdArray(
            int equipmentId,
            int[] dataTypeIdArray,
            Timestamp start,
            Timestamp end
    );
}
