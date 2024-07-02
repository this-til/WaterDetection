package com.til.water_detection.fictitious_data;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.til.water_detection.api.IAPI;
import com.til.water_detection.data.*;
import com.til.water_detection.data.state.ResultType;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Void.class, (InstanceCreator<Void>) type -> null);
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson)) // 如果你使用Gson
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 如果你使用RxJava 2.x
                .build();

        IAPI iapi = retrofit.create(IAPI.class);

        Response<Result<String>> login = iapi.login("til", "114514").execute();
        assert login.isSuccessful();
        Result<String> loginBody = login.body();

        assert loginBody != null;
        assert loginBody.getResultType() == ResultType.SUCCESSFUL;
        assert loginBody.getData() != null;


        Response<Result<List<DataType>>> allDataTypeExecute = iapi.getAllDataType(loginBody.getData()).execute();

        assert allDataTypeExecute.isSuccessful();

        Result<List<DataType>> allDataTypeBody = allDataTypeExecute.body();
        assert allDataTypeBody != null;
        assert allDataTypeBody.getResultType() == ResultType.SUCCESSFUL;
        assert allDataTypeBody.getData() != null;
        assert !allDataTypeBody.getData().isEmpty();


        while (true) {


            iapi.addDataList(loginBody.getData(), List.of(
                    new Data(0, 24, 9, null, 26),
                    new Data(0, 24, 10, null, 7),
                    new Data(0, 24, 11, null, 10),
                    new Data(0, 24, 12, null, 2000)
            )).execute();

            Thread.sleep(1000 * 3);
        }

    }

}