package com.prox.babyvaccinationtracker.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.prox.babyvaccinationtracker.geo.district;
import com.prox.babyvaccinationtracker.geo.province;
import com.prox.babyvaccinationtracker.geo.ward;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GeoApi {

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    GeoApi geoapi = new Retrofit.Builder()
            .baseUrl("https://provinces.open-api.vn/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(GeoApi.class);
    @GET("api/")
    Call<JsonArray> all_provinces();
    @GET("api/p/{id}?depth=2")
    Call<province> all_districts(@Path("id") int provinceID);
    @GET("api/d/{id}?depth=2")
    Call<district> all_wards(@Path("id") int districtID);


    @GET("api/p/{id}")
    Call<province> province(@Path("id") int groupId);
    @GET("api/d/{id}")
    Call<district> district(@Path("id") int groupId,
                            @Query("depth")  int code);
    @GET("api/w/{id}")
    Call<ward> ward(@Path("id") int groupId,
                    @Query("depth") int code);
}
