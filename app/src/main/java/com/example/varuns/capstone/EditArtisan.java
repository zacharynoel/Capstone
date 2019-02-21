package com.example.varuns.capstone;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.varuns.capstone.model.Artisan;
import com.example.varuns.capstone.services.ApiService;
import com.example.varuns.capstone.services.RestfulResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditArtisan extends AppCompatActivity {

    Artisan artisan;
    EditText nameinput;
    EditText bioinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_artisan);
        int id = this.getIntent().getExtras().getInt("artisanId");
        getArtisanById(id);
    }

    public void cancelEditArt(View v){
        finish();
    }

    public void addArt(View v){
        String[] names = nameinput.getText().toString().split(" ");
        artisan.setFirstName(names[0]);
        artisan.setLastName(names[1]);
        artisan.setBio(bioinput.getText().toString());

        //call save artisan function of artisan service
        //when saving what ever you saved is returned with updated ids and other fields
        Call<RestfulResponse<Artisan>> call1 = ApiService.artisanService().saveArtisan(artisan);
        call1.enqueue(new Callback<RestfulResponse<Artisan>>() {
            @Override
            public void onResponse(Call<RestfulResponse<Artisan>> call, Response<RestfulResponse<Artisan>> response) {
                //report the result of the call
                Toast.makeText(EditArtisan.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                //artisan.setArtisanId(response.body().getData().getArtisanId());
            }

            @Override
            public void onFailure(Call<RestfulResponse<Artisan>> call, Throwable t) {
                Toast.makeText(EditArtisan.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        menu_activity.artName.setText(artisan.getFirstName()+" " +artisan.getLastName());
        ScrollingActivity.artisanBio.setText(artisan.getBio());
        ScrollingActivity.artisanName.setText(artisan.getFirstName()+" " +artisan.getLastName());
        finish();
    }

    private void getArtisanById(final Integer artisanId) {

        Call<RestfulResponse<Artisan>> call = ApiService.artisanService().getArtisanById(artisanId.toString());
        //handle the response
        call.enqueue(new Callback<RestfulResponse<Artisan>>() {
            @Override
            public void onResponse(Call<RestfulResponse<Artisan>> call, Response<RestfulResponse<Artisan>> response) {
                artisan = response.body().getData();

                nameinput = (EditText) findViewById(R.id.editName2);
                bioinput = (EditText) findViewById(R.id.editBio2);
                nameinput.setText(artisan.getFirstName()+" "+artisan.getLastName());
                bioinput.setText(artisan.getBio());
            }

            @Override
            public void onFailure(Call<RestfulResponse<Artisan>> call, Throwable t) {
                Toast.makeText(EditArtisan.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
