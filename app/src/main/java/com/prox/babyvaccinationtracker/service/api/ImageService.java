package com.prox.babyvaccinationtracker.service.api;

import android.graphics.Bitmap;

import com.prox.babyvaccinationtracker.response.UploadImageResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ImageService {
//    @FormUrlEncoded
    @Multipart
    @POST("/1/upload")
    Call<UploadImageResponse> uploadImage(
            @Part() MultipartBody.Part image,
            @Query("key") String key
    );
}

