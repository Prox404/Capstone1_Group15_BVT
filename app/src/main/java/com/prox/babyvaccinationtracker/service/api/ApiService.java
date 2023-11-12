package com.prox.babyvaccinationtracker.service.api;

import com.prox.babyvaccinationtracker.response.BotResponse;
import com.prox.babyvaccinationtracker.response.SideEffectResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("/ask/side-effect")
    Call<SideEffectResponse> sendSideEffect(
            @Field("message") String message,
            @Field("sideEffect") String sideEffect
    );
    @FormUrlEncoded
    @POST("/ask")
    Call<BotResponse> ask(@Field("message") String message);
}
