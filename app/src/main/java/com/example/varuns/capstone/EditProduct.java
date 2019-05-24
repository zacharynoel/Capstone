package com.example.varuns.capstone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.varuns.capstone.model.Artisan;
import com.example.varuns.capstone.model.ArtisanItem;
import com.example.varuns.capstone.services.ApiService;
import com.example.varuns.capstone.services.RestfulResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProduct extends BottomNavigation {

    Artisan artisan;
    TextInputEditText descInput;
    TextInputEditText nameInput;
    TextInputEditText priceInput;
    List<ArtisanItem> items;
    ArtisanItem item;
    Integer artisanId;
    Integer itemId;
    ImageButton imgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        setTitle(R.string.editproduct);

        artisanId = getIntent().getExtras().getInt("artisanId");
        itemId = getIntent().getExtras().getInt("itemId");

        nameInput = (TextInputEditText) findViewById(R.id.namebox);
        descInput = (TextInputEditText) findViewById(R.id.descbox);
        priceInput = (TextInputEditText) findViewById(R.id.pricebox);

        getArtisanById(artisanId);

        imgButton = (ImageButton) findViewById(R.id.imageButton2);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        setupBottomNavigationView(EditProduct.this);
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

    //this function takes the text inputted into the name and desc boxes and updates an ArtisanItem with it
    public void saveItem(View v){
        if(!verifyFields()){
            //if user tries to edit item so there is no name then an error is set
            return;
        }
        item.setItemName(nameInput.getText().toString());
        item.setItemDescription(descInput.getText().toString());
        item.setPrice(new BigDecimal(priceInput.getText().toString()));

        Bitmap bitmap = ((BitmapDrawable)imgButton.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        item.setEncodedImage(encoded);

        //this call will update and save the contents of the artisan that is passed to it
        Call<RestfulResponse<ArtisanItem>> call1 = ApiService.itemService().saveArtisanItem1(item);
        call1.enqueue(new Callback<RestfulResponse<ArtisanItem>>() {
            @Override
            public void onResponse(Call<RestfulResponse<ArtisanItem>> call, Response<RestfulResponse<ArtisanItem>> response) {
                //report the result of the call
                Toast.makeText(EditProduct.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

            }

            //if the call results in a failure
            @Override
            public void onFailure(Call<RestfulResponse<ArtisanItem>> call, Throwable t) {
                Toast.makeText(EditProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        finish();

    }

    //ends this activity and changes nothing if the cancel button is pressed
    public void cancelEdit(View v){
        finish();
    }

    //searches for an artisan by the artisanId number, artisan is saved to Artisan object
    private void getArtisanById(final Integer artisanId) {

        Call<RestfulResponse<Artisan>> call = ApiService.artisanService().getArtisanById(artisanId.toString());
        //handle the response
        call.enqueue(new Callback<RestfulResponse<Artisan>>() {
            @Override
            public void onResponse(Call<RestfulResponse<Artisan>> call, Response<RestfulResponse<Artisan>> response) {
                //gets artisan, finds appropriate item, sets text fields to item name and description
                artisan = response.body().getData();

                items = artisan.getArtisanItems();
                item = items.get(itemId);

                if (item.getItemName() != null) {
                    nameInput.setText(item.getItemName());
                }
                if (item.getItemDescription() != null) {
                    descInput.setText(item.getItemDescription());
                }
                if (item.getPrice() != null) {
                    priceInput.setText(item.getPrice().toString());
                }
                if (item.getEncodedImage() != null) {
                    byte[] decodedString = Base64.decode(item.getEncodedImage(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imgButton.setImageBitmap(decodedByte);
                }
            }

            //response if call results in a failure
            @Override
            public void onFailure(Call<RestfulResponse<Artisan>> call, Throwable t) {
                Toast.makeText(EditProduct.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //verifies that user inputted correct information
    private boolean verifyFields() {
        nameInput.setError(null);
        if (nameInput.getText().length() == 0) {
            nameInput.setError("Item name is required!");
            return false;
        }

        return true;
    }
}
