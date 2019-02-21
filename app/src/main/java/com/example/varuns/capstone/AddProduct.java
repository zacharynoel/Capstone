package com.example.varuns.capstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.varuns.capstone.model.Artisan;
import com.example.varuns.capstone.model.ArtisanItem;
import com.example.varuns.capstone.services.ApiService;
import com.example.varuns.capstone.services.RestfulResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProduct extends AppCompatActivity {

    Artisan artisan;
    EditText itemname;
    EditText itemdesc;
    Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        id = this.getIntent().getExtras().getInt("artisanId");
        getArtisanById(id);

        itemname = (EditText) findViewById(R.id.addProductName);
        itemdesc = (EditText) findViewById(R.id.addProductDesc);

    }

    public void cancelAddProd(View v){
        finish();
    }

    public void saveProd(View v){
        List<ArtisanItem> items = artisan.getArtisanItems();
        ArtisanItem newItem = new ArtisanItem(id, items.size()+1, itemname.getText().toString(), itemdesc.getText().toString());
        items.add(newItem);
        artisan.setArtisanItems(items);

        //call save artisan function of artisan service
        //when saving what ever you saved is returned with updated ids and other fields
        Call<RestfulResponse<Artisan>> call1 = ApiService.artisanService().saveArtisan(this.artisan);
        call1.enqueue(new Callback<RestfulResponse<Artisan>>() {
            @Override
            public void onResponse(Call<RestfulResponse<Artisan>> call, Response<RestfulResponse<Artisan>> response) {
                //report the result of the call
                Toast.makeText(AddProduct.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                //artisan.setArtisanId(response.body().getData().getArtisanId());
            }

            @Override
            public void onFailure(Call<RestfulResponse<Artisan>> call, Throwable t) {
                Toast.makeText(AddProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //ScrollingActivity.getAdapter().addItem(newItem);
        finish();
    }

    private void getArtisanById(final Integer artisanId) {

        Call<RestfulResponse<Artisan>> call = ApiService.artisanService().getArtisanById(artisanId.toString());
        //handle the response
        call.enqueue(new Callback<RestfulResponse<Artisan>>() {
            @Override
            public void onResponse(Call<RestfulResponse<Artisan>> call, Response<RestfulResponse<Artisan>> response) {
                artisan = response.body().getData();
            }

            @Override
            public void onFailure(Call<RestfulResponse<Artisan>> call, Throwable t) {
                Toast.makeText(AddProduct.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
