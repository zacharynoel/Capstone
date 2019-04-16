package com.example.varuns.capstone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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
    EditText nameInput;
    EditText bioInput;
    EditText phoneInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_artisan);
        setTitle("Edit Artisan");
        int id = this.getIntent().getExtras().getInt("artisanId");
        getArtisanById(id);
        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent = new Intent(EditArtisan.this, menu_activity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent1 = new Intent(EditArtisan.this, Send_message.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent2 = new Intent(EditArtisan.this, ReportsActivity.class);
                        startActivity(intent2);
                        break;

                }
                return true;
            }
        });
    }


    public void cancelEditArtisan(View v){
        finish();
    }

    public void nameError(String errorMsg) {
        nameInput.setError(errorMsg);
    }

    public void addArtisan(View v) {
        String[] names = nameInput.getText().toString().split(" ");
        if (names.length == 0) {
            nameError("Name is required!");
            return;
        }

        if (names.length > 2) {
            nameError("Name can only be First OR First Last");
            return;
        }

        artisan.setFirstName(names[0]);

        if (names.length == 2) {
            artisan.setLastName(names[1]);
        }
        artisan.setBio(bioInput.getText().toString());
        artisan.setPhoneNo(phoneInput.getText().toString());

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

                nameInput = (EditText) findViewById(R.id.editName2);
                bioInput = (EditText) findViewById(R.id.editBio2);
                phoneInput = (EditText) findViewById(R.id.editPhoneNumber2);
                nameInput.setText(artisan.getFirstName()+" "+artisan.getLastName());
                bioInput.setText(artisan.getBio());
                phoneInput.setText(artisan.getPhoneNo());
            }

            @Override
            public void onFailure(Call<RestfulResponse<Artisan>> call, Throwable t) {
                Toast.makeText(EditArtisan.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
