package com.example.varuns.capstone.services;

import com.example.varuns.capstone.model.Artisan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ArtisanService {

    @GET("artisan/{artisanId}")
    Call<RestfulResponse<Artisan>> getArtisanById(@Path("artisanId") String artisanId);

    @GET("artisan/all")
    Call<RestfulResponse<List<Artisan>>> getAllArtisans();

    @POST("artisan")
    Call<RestfulResponse<Artisan>> saveArtisan(@Body Artisan artisan);

}
