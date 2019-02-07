package com.example.varuns.capstone;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varuns.capstone.model.Artisan;
import com.example.varuns.capstone.model.ArtisanItem;
import com.example.varuns.capstone.services.ApiService;
import com.example.varuns.capstone.services.RestfulResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProduct extends AppCompatActivity {

    Artisan artisan;
    TextInputEditText descinput;
    TextInputEditText nameinput;
    List<ArtisanItem> items;
    Integer artisanId;
    Integer itemId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        artisanId = getIntent().getExtras().getInt("artisanId");
        itemId = getIntent().getExtras().getInt("itemId");

        getArtisanById(artisanId);

    }

    public void saveItem(View v){
        items.get(itemId).setItemName(nameinput.getText().toString());
        items.get(itemId).setItemDescription(descinput.getText().toString());

        Call<RestfulResponse<Artisan>> call1 = ApiService.artisanService().saveArtisan(artisan);
        call1.enqueue(new Callback<RestfulResponse<Artisan>>() {
            @Override
            public void onResponse(Call<RestfulResponse<Artisan>> call, Response<RestfulResponse<Artisan>> response) {
                //report the result of the call
                Toast.makeText(EditProduct.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                //artisan.setArtisanId(response.body().getData().getArtisanId());
            }

            @Override
            public void onFailure(Call<RestfulResponse<Artisan>> call, Throwable t) {
                Toast.makeText(EditProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        finish();

    }

    public void cancelEdit(View v){
        finish();
    }

    private void getArtisanById(final Integer artisanId) {

        Call<RestfulResponse<Artisan>> call = ApiService.artisanService().getArtisanById(artisanId.toString());
        //handle the response
        call.enqueue(new Callback<RestfulResponse<Artisan>>() {
            @Override
            public void onResponse(Call<RestfulResponse<Artisan>> call, Response<RestfulResponse<Artisan>> response) {
                artisan = response.body().getData();

                items = artisan.getArtisanItems();

                nameinput = (TextInputEditText) findViewById(R.id.namebox);
                descinput = (TextInputEditText) findViewById(R.id.descbox);
                nameinput.setText(items.get(itemId).getItemName());
                descinput.setText(items.get(itemId).getItemDescription());
            }

            @Override
            public void onFailure(Call<RestfulResponse<Artisan>> call, Throwable t) {
                Toast.makeText(EditProduct.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
