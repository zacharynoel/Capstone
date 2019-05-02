package com.example.varuns.capstone.services;

import com.example.varuns.capstone.model.Artisan;
import com.example.varuns.capstone.model.SessionItem;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("session")
    Call<RestfulResponse<SessionItem>> attemptLogin(@Body HashMap userLoginObj);
}
