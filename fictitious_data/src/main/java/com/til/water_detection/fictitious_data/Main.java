package com.til.water_detection.fictitious_data;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.til.water_detection.api.IAPI;
import com.til.water_detection.data.*;
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

        Response<Result<List<Equipment>>> allEquipmentExecute = iapi.getAllEquipment().execute();

        assert allEquipmentExecute.isSuccessful();

        Result<List<Equipment>> allEquipmentBody = allEquipmentExecute.body();

        assert allEquipmentBody != null;
        assert allEquipmentBody.getResultType() == ResultType.SUCCESSFUL;
        assert allEquipmentBody.getData() != null;
        assert !allEquipmentBody.getData().isEmpty();


        Response<Result<List<DataType>>> allDataTypeExecute = iapi.getAllDataType().execute();

        assert allDataTypeExecute.isSuccessful();

        Result<List<DataType>> allDataTypeBody = allDataTypeExecute.body();
        assert allDataTypeBody != null;
        assert allDataTypeBody.getResultType() == ResultType.SUCCESSFUL;
        assert allDataTypeBody.getData() != null;
        assert !allDataTypeBody.getData().isEmpty();

        List<Equipment> allEquipment = allEquipmentBody.getData();
        List<DataType> allDataType = allDataTypeBody.getData();


        PerlinNoise perlinNoise = new PerlinNoise();
        List<Pack> packList = new ArrayList<>();

        float i = 0;
        for (Equipment equipment : allEquipment) {
            for (DataType dataType : allDataType) {
                packList.add(new Pack(perlinNoise, equipment, dataType, i));
                i  += 0.1f;
            }
        }

        List<Data> dataList = new ArrayList<>();
        while (true) {

            for (Pack pack : packList) {
                dataList.add(new Data(
                        0,
                        pack.getEquipment().getId(),
                        pack.getDataType().getId(),
                        null,
                        pack.nextValue()));
            }

           /* for (Data data : dataList) {
                iapi.addData(data).execute();
            }*/


            Response<Result<Void>> execute = iapi.addDataList(dataList).execute();

            assert execute.isSuccessful();
            assert execute.body() != null;
            assert execute.body().getResultType() == ResultType.SUCCESSFUL;


            dataList.clear();

            Thread.sleep(1000 * 3);
        }

    }

    public static class Pack {

        private final PerlinNoise perlinNoise;

        private final Equipment equipment;
        private final DataType dataType;
        private final float y;
        private float x;

        public Pack(PerlinNoise perlinNoise, Equipment equipment, DataType dataType, float y) {
            this.perlinNoise = perlinNoise;
            this.equipment = equipment;
            this.dataType = dataType;
            this.y = y;
        }

        public float nextValue() {
            x += 0.05f;
            return (float) perlinNoise.noise(x, y);
        }


        public Equipment getEquipment() {
            return equipment;
        }

        public DataType getDataType() {
            return dataType;
        }
    }

}