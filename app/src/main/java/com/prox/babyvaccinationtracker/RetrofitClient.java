package com.prox.babyvaccinationtracker;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        // Tạo interceptor để ghi log yêu cầu và phản hồi
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        // Khởi tạo Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.26.208.1:3000/")  // Thay đổi URL tương ứng với API của bạn
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}

