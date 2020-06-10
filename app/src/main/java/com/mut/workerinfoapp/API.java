package com.mut.workerinfoapp;

import com.mut.workerinfoapp.domain.CountBean;
import com.mut.workerinfoapp.domain.Workerbean;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API  {
    @GET("record")
    Call<Workerbean> getworkrinout();

    @GET("count")
    Call<CountBean> getCount();
}
