package com.sneha.livestreaming.controller;

import com.sneha.livestreaming.model.AllChannedlModel;
import com.sneha.livestreaming.model.AllDataModel;
import com.sneha.livestreaming.model.CommonModel;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("token")
    Single<CommonModel> sendToken(@Field("access_token") String access_token);

    @GET("getdata")
    Single<AllDataModel> getAllData();

    @GET("getchannels")
    Single<AllChannedlModel> getAllChannelData();



}
