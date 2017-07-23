package com.miss.meet.model.http;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by linmu on 2017/6/13.
 */

public interface ApiService {


    //  1. 传送图片

    @Multipart
    @POST("/PicService/PicS")
    Call<Result<String>> uploadPicture(@Part List<MultipartBody.Part> partList);


    @Multipart
    @POST("you methd url upload/")
    Call<ResponseBody> uploadFile(
            @Part("avatar\"; filename=\"avatar.jpg") RequestBody file);

}
