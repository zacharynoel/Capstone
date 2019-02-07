package com.example.varuns.capstone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varuns.capstone.model.Artisan;
import com.example.varuns.capstone.services.ApiService;
import com.example.varuns.capstone.services.RestfulResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddArtisanActivity extends AppCompatActivity {
    private EditText editName;
    private EditText editPhoneNumber;
    private EditText editBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artisan);

        editName = (EditText) findViewById(R.id.editName);
        editPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        editBio = (EditText) findViewById(R.id.editBio);
    }

    public void addNewArtisan(View view) {
        if (!verifyFields()) {
            //user has not correctly inputted all fields
            return;
        }
        String name = editName.getText().toString();
        String phoneNumber = editPhoneNumber.getText().toString();
        String bio = editBio.getText().toString();

        //create artisan to save
        final Artisan artisan = new Artisan();
        String[] names = name.split(" ");
        artisan.setFirstName(names[0]);
        artisan.setLastName(names[1]);
        artisan.setBio(bio);

        //call save artisan function of artisan service
        //when saving what ever you saved is returned with updated ids and other fields
        Call<RestfulResponse<Artisan>> call1 = ApiService.artisanService().saveArtisan(artisan);
        call1.enqueue(new Callback<RestfulResponse<Artisan>>() {
            @Override
            public void onResponse(Call<RestfulResponse<Artisan>> call, Response<RestfulResponse<Artisan>> response) {
                //report the result of the call
                Toast.makeText(AddArtisanActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                artisan.setArtisanId(response.body().getData().getArtisanId());
            }

            @Override
            public void onFailure(Call<RestfulResponse<Artisan>> call, Throwable t) {
                Toast.makeText(AddArtisanActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        menu_activity.getAdapter().addArtisan(artisan);
        finish();
    }

    //verifies that user inputted correct information
    private boolean verifyFields() {
        editName.setError(null);
        if (editName.getText().length() == 0) {
            editName.setError("Name is required!");
            return false;
        }

        return true;
    }
}
