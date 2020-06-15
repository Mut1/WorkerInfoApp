package com.mut.workerinfoapp.Utils;

import com.blankj.utilcode.util.NetworkUtils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {


    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.102:8080/inoutrecord/")
           // .baseUrl("http://"+ NetworkUtils.getServerAddressByWifi()+":8080/inoutrecord/")

            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static Retrofit getRetrofit() {
        return retrofit;
    }


}
