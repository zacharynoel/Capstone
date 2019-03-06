package com.example.varuns.capstone.services;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    public static Retrofit getRetrofitInstance() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/api/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public static ArtisanService artisanService() {
        Retrofit retrofit = getRetrofitInstance();
        return retrofit.create(ArtisanService.class);
    }

    public static UserService userService() {
        Retrofit retrofit = getRetrofitInstance();
        return retrofit.create(UserService.class);
    }

    public static ItemService itemService() {
        Retrofit retrofit = getRetrofitInstance();
        return retrofit.create(ItemService.class);
    }

}
