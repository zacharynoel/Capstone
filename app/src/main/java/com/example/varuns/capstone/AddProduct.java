package com.example.varuns.capstone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.varuns.capstone.model.Artisan;
import com.example.varuns.capstone.model.ArtisanItem;
import com.example.varuns.capstone.services.ApiService;
import com.example.varuns.capstone.services.RestfulResponse;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProduct extends AppCompatActivity {

    Artisan artisan;
    EditText itemname;
    EditText itemdesc;
    ImageButton imgButton;
    Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        setTitle(R.string.addproduct);

        id = this.getIntent().getExtras().getInt("artisanId");
        getArtisanById(id);

        itemname = (EditText) findViewById(R.id.addProductName);
        itemdesc = (EditText) findViewById(R.id.addProductDesc);

        imgButton = (ImageButton) findViewById(R.id.addProductPic);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        setupBottomNavigationView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            if (intent != null) {
                final Uri uri = intent.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imgButton.setImageBitmap(bitmap);
                } catch(IOException e) {
                    System.out.println("Error, cannot find image file");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent = new Intent(AddProduct.this, menu_activity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent1 = new Intent(AddProduct.this, Send_message.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent2 = new Intent(AddProduct.this, ReportsActivity.class);
                        startActivity(intent2);
                        break;

                }
                return true;
            }
        });
    }

    public void cancelAddProd(View v){
        finish();
    }

    public void saveProd(View v){
        if (!verifyFields()){
            return;
        }
        Bitmap bitmap = ((BitmapDrawable)imgButton.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);



        List<ArtisanItem> items = artisan.getArtisanItems();
        ArtisanItem newItem = new ArtisanItem(id, items.size()+1, itemname.getText().toString(), itemdesc.getText().toString());
        newItem.setEncodedImage(encoded);
        items.add(newItem);
        artisan.setArtisanItems(items);

        //call save artisan function of artisan service
        //when saving what ever you saved is returned with updated ids and other fields
        Call<RestfulResponse<ArtisanItem>> call1 = ApiService.itemService().saveArtisanItem1(newItem);
        call1.enqueue(new Callback<RestfulResponse<ArtisanItem>>() {
            @Override
            public void onResponse(Call<RestfulResponse<ArtisanItem>> call, Response<RestfulResponse<ArtisanItem>> response) {
                //report the result of the call
                Toast.makeText(AddProduct.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                //artisan.setArtisanId(response.body().getData().getArtisanId());
            }

            @Override
            public void onFailure(Call<RestfulResponse<ArtisanItem>> call, Throwable t) {
                Toast.makeText(AddProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent();
        intent.putExtra("productArtisan", newItem.getArtisanId());
        intent.putExtra("productId", newItem.getItemId());
        intent.putExtra("productName", newItem.getItemName());
        intent.putExtra("productDesc", newItem.getItemDescription());
        setResult(RESULT_OK, intent);
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

    private boolean verifyFields(){
        if (itemname.getText().length()==0) {
            itemname.setError("Item name is required!");
            return false;
        }
        return true;
    }
}
