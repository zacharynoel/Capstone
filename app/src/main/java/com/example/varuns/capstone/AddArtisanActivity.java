package com.example.varuns.capstone;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varuns.capstone.model.Artisan;
import com.example.varuns.capstone.services.ApiService;
import com.example.varuns.capstone.services.RestfulResponse;

import java.io.IOException;

import okio.Buffer;
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

        setTitle(R.string.addartisan);
        setContentView(R.layout.activity_add_artisan);

        editName = (EditText) findViewById(R.id.editName);
        editPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        editBio = (EditText) findViewById(R.id.editBio);

        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent = new Intent(AddArtisanActivity.this, menu_activity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent1 = new Intent(AddArtisanActivity.this, Send_message.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent2 = new Intent(AddArtisanActivity.this, ReportsActivity.class);
                        startActivity(intent2);
                        break;

                }
                return true;
            }
        });
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
        if (names.length > 2) {
            editName.setError("Name can only be First OR First Last");
            return;
        }

        if(!phoneNumber.matches("\\d+")) {
            editPhoneNumber.setError("Phone number must only contain digits");
            return;
        }

        if (names[0].matches("\\d+")) {
            editName.setError("Name should not contain any numbers");
            return;
        }

        artisan.setFirstName(names[0]);

        if (names.length == 2) {
            if (names[1].matches("\\d+")) {
                editName.setError("Name should not contain any numbers");
                return;
            }

            artisan.setLastName(names[1]);
        }
        else {
            artisan.setLastName("");
        }
        artisan.setBio(bio);
        artisan.setPhoneNo(phoneNumber);

        //call save artisan function of artisan service
        //when saving what ever you saved is returned with updated ids and other fields
        Call<RestfulResponse<Artisan>> call1 = ApiService.artisanService().saveArtisan(artisan);
        call1.enqueue(new Callback<RestfulResponse<Artisan>>() {
            @Override
            public void onResponse(Call<RestfulResponse<Artisan>> call, Response<RestfulResponse<Artisan>> response) {
                //report the result of the call
                try {
                    Buffer buffer = new Buffer();
                    call.request().body().writeTo(buffer);
                    System.out.println("Caller: " + buffer.readUtf8());
                } catch(IOException e) {

                }
                System.out.println("Reciever: " + response.raw().toString());
                artisan.setArtisanId(response.body().getData().getArtisanId());
            }

            @Override
            public void onFailure(Call<RestfulResponse<Artisan>> call, Throwable t) {
                Toast.makeText(AddArtisanActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if (menu_activity.getAdapter() != null)
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
        if (editName.getText().length()>45){
            editName.setError("Name is too long!");
            return false;
        }

        if (editPhoneNumber.getText().length()==0) {
            editPhoneNumber.setError("Phone number is required!");
            return false;
        }
        if (editPhoneNumber.getText().length()>20) {
            editPhoneNumber.setError("Phone number is too long!");
            return false;
        }

        return true;
    }
}
