package com.example.varuns.capstone.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Date;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static String token = "";

    public static void setToken(String newToken) {token = newToken;}
    public static void clearToken() {token = "";}
    public static boolean hasToken() {return !token.equals("");}

    public static Retrofit getRetrofitInstance() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", token)
                        .build();
                return chain.proceed(request);
            }
        });

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build());
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

    public static LoginService loginService() {
        Retrofit retrofit = getRetrofitInstance();
        return retrofit.create(LoginService.class);
    }
}
