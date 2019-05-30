package com.example.varuns.capstone.services;

import com.example.varuns.capstone.model.Artisan;
import com.example.varuns.capstone.model.ArtisanItem;
import com.example.varuns.capstone.model.ItemCategory;
import com.example.varuns.capstone.model.SoldItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ItemService {

    @GET("api/artisan-item/sold-item/artisan-id/{artisanId}")
    Call<RestfulResponse<List<SoldItem>>> getSoldItemsByArtisanId(@Path("artisanId") String artisanId);

    @GET("api/artisan-item/sold-item/item-id/{itemId}")
    Call<RestfulResponse<SoldItem>> getSoldItemsByArtisanItemId(@Path("itemId") String itemId);

    @POST("api/artisan-item/sold-item")
    Call<RestfulResponse<Integer>> saveSoldItem(@Body SoldItem soldItem);

    @POST("api/artisan-item")
    Call<RestfulResponse<ArtisanItem>> saveArtisanItem1(@Body ArtisanItem artisanItem);

    @POST("api/artisan-item/sold-item")
    Call<RestfulResponse<Integer>> saveArtisanItem(@Body ArtisanItem soldItem);

    @GET("api/artisan-item/sold-item/user-id/{userId}")
    Call<RestfulResponse<List<SoldItem>>> getSoldItemsByUserId(@Path("userId") String userId);

    @GET("api/artisan-item/item-categories")
    Call<RestfulResponse<List<ItemCategory>>> getAllItemCategories();



}
