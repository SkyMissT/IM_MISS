package com.miss.meet.model.http;

import com.miss.meet.constant.Constant;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.Result;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by linmu on 2017/6/13.
 */

public class RetrofitHelper {

    private static final int READ_TIMEOUT = 12;
    private static final int CONN_TIMEOUT = 12;

    private static Retrofit mRetrofit;

    private RetrofitHelper(){}

    public static  Retrofit getInstance(){

        mRetrofit = null;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        builder.connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();

        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return mRetrofit;
    }

    private static ApiService getApiService(){
        return RetrofitHelper.getInstance().create(ApiService.class);
    }

    public static Call<Result<String>> uploadPicture(List<MultipartBody.Part> partList){
        return RetrofitHelper.getApiService().uploadPicture(partList);
    }

}
