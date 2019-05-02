package com.example.varuns.capstone.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {

    @GET("api/authentication/user/{userId}")
    Call<RestfulResponse> getUserById(@Path("userId") String userId);

}
