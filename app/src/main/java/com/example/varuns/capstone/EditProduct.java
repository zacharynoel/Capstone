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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varuns.capstone.Util.ImageUtil;
import com.example.varuns.capstone.model.Artisan;
import com.example.varuns.capstone.model.ArtisanItem;
import com.example.varuns.capstone.model.ItemCategory;
import com.example.varuns.capstone.services.ApiService;
import com.example.varuns.capstone.services.RestfulResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Artisan artisan;
    TextInputEditText descInput;
    TextInputEditText nameInput;
    TextInputEditText priceInput;
    List<ArtisanItem> items;
    ArtisanItem item;
    Integer artisanId;
    Integer itemId;
    ImageButton imgButton;
    List<ItemCategory> itemCategories;
    Spinner spinner;
    ItemCategory itemCategory;

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
        spinner = (Spinner) findViewById(R.id.categorySpinner);


        getArtisanById(artisanId);
        getItemCategories();

        imgButton = (ImageButton) findViewById(R.id.imageButton2);

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
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    private void setCategorySpinnerOptions(List<ItemCategory> itemCategories) {

        // Create an ArrayAdapter using the artisans array and a default spinner layout
        EditProduct.ItemCategoryAdapter itemCategoryAdapter = new EditProduct.ItemCategoryAdapter(itemCategories);

        // Specify the layout to use when the list of choices appears
        // Apply the adapter to the spinner
        spinner.setAdapter(itemCategoryAdapter);
        spinner.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        itemCategory = (ItemCategory)parent.getItemAtPosition(pos);

    }

    private void getItemCategories() {
        Call<RestfulResponse<List<ItemCategory>>> call = ApiService.itemService().getAllItemCategories();
        //handle the response
        call.enqueue(new Callback<RestfulResponse<List<ItemCategory>>>() {
            @Override
            public void onResponse(Call<RestfulResponse<List<ItemCategory>>> call, Response<RestfulResponse<List<ItemCategory>>> response) {
                itemCategories = response.body().getData();
                setCategorySpinnerOptions(itemCategories);

            }

            @Override
            public void onFailure(Call<RestfulResponse<List<ItemCategory>>> call, Throwable t) {
                Toast.makeText(EditProduct.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent = new Intent(EditProduct.this, menu_activity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent1 = new Intent(EditProduct.this, Send_message.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent2 = new Intent(EditProduct.this, ReportsActivity.class);
                        startActivity(intent2);
                        break;

                }
                return true;
            }
        });
    }

    //this function takes the text inputted into the name and desc boxes and updates an ArtisanItem with it
    public void saveItem(View v){
        if(!verifyFields()){
            //if user tries to edit item so there is no name then an error is set
            return;
        }
        item.setItemName(nameInput.getText().toString());
        item.setItemDescription(descInput.getText().toString());

        if(!priceInput.getText().toString().isEmpty() && Double.parseDouble(priceInput.getText().toString()) >= 0)
            item.setPrice(new BigDecimal(priceInput.getText().toString()));
        else
            item.setPrice(new BigDecimal(0));

        if (itemCategory != null) {
            item.setItemCategory(itemCategory);
        }

        Bitmap bitmap = ((BitmapDrawable)imgButton.getDrawable()).getBitmap();
        String encoded = ImageUtil.BitmapToEncodedString(bitmap);
        item.setEncodedImage(encoded);

        //this call will update and save the contents of the artisan that is passed to it
        Call<RestfulResponse<ArtisanItem>> call1 = ApiService.itemService().saveArtisanItem1(item);
        call1.enqueue(new Callback<RestfulResponse<ArtisanItem>>() {
            @Override
            public void onResponse(Call<RestfulResponse<ArtisanItem>> call, Response<RestfulResponse<ArtisanItem>> response) {
                //report the result of the call
                Toast.makeText(EditProduct.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent  = new Intent();
                intent.putExtra("artisanId", artisan.getArtisanId());
                setResult(RESULT_OK, intent);
                finish();




            }

            //if the call results in a failure
            @Override
            public void onFailure(Call<RestfulResponse<ArtisanItem>> call, Throwable t) {
                Toast.makeText(EditProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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

    class ItemCategoryAdapter extends BaseAdapter {

        List<ItemCategory> itemCategories;

        public ItemCategoryAdapter(List<ItemCategory> itemCategories) {

            this.itemCategories = itemCategories;
        }

        public int getCount() {
            return itemCategories.size();
        }
        public ItemCategory getItem(int i) {
            return itemCategories.get(i);
        }
        public void addItem(ItemCategory itemCategory) { itemCategories.add(itemCategory);}
        public long getItemId(int i) {
            return itemCategories.get(i).getCategoryId();
        }
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.category_layout, null);
            TextView categoryDescription = (TextView)view.findViewById(R.id.categoryDescription);
            categoryDescription.setText(itemCategories.get(i).getDescription());
            return view;
        }

    }
}
