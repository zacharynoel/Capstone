package com.example.varuns.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
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

public class ScrollingActivity extends AppCompatActivity {

    static Artisan artisan;
    private static ArtisanProductAdapter artisanProductAdapterGlobal;
    static TextView artisanBio;
    static TextView artisanName;
    static TextView prodName;
    static TextView prodDesc;
    private Integer[] artisanImages = {R.drawable.maria, R.drawable.native5, R.drawable.native3 };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        artisanBio = (TextView) findViewById(R.id.artisanBio);
        artisanName = (TextView) findViewById(R.id.artisanName);
        Integer artisanId = getIntent().getExtras().getInt("artisanId");
        getArtisanById(artisanId);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(ScrollingActivity.this, Send_message.class);
                myintent.putExtra("phoneNo", artisan.getPhoneNo());
                startActivity(myintent);
            }
        });

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
                        Intent intent = new Intent(ScrollingActivity.this, menu_activity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent1 = new Intent(ScrollingActivity.this, Send_message.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent2 = new Intent(ScrollingActivity.this, ReportsActivity.class);
                        startActivity(intent2);
                        break;

                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void editProductClick(View v)
    {
        View parentRow = (View) v.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);

        Intent intent = new Intent(ScrollingActivity.this, EditProduct.class);
        intent.putExtra("artisanId", artisan.getArtisanId());
        intent.putExtra("itemId", position);
        prodName = (TextView) parentRow.findViewById(R.id.itemName);
        prodDesc = (TextView) parentRow.findViewById(R.id.itemDescription);
        startActivity(intent);
    }

    public void editArtisanClick(View v){
        Intent intent = new Intent(ScrollingActivity.this, EditArtisan.class);
        intent.putExtra("artisanId", artisan.getArtisanId());
        startActivity(intent);
    }

    public void addItemClick(View v){
        Intent intent = new Intent(ScrollingActivity.this, AddProduct.class);
        intent.putExtra("artisanId", artisan.getArtisanId());
        startActivity(intent);
    }

    public void onReportClick(View v){
        Intent intent = new Intent(ScrollingActivity.this, ReportsActivity.class);
        intent.putExtra("artisanId", artisan.getArtisanId());
        startActivity(intent);
    }

    private void getArtisanById(final Integer artisanId) {

        Call<RestfulResponse<Artisan>> call = ApiService.artisanService().getArtisanById(artisanId.toString());
        //handle the response
        call.enqueue(new Callback<RestfulResponse<Artisan>>() {
            @Override
            public void onResponse(Call<RestfulResponse<Artisan>> call, Response<RestfulResponse<Artisan>> response) {
                artisan = response.body().getData();
                ImageView imageView = (ImageView)findViewById(R.id.imageButton);
                imageView.setImageResource(artisanImages[(artisan.getArtisanId() - 1)%3]);
                artisanBio = (TextView) findViewById(R.id.artisanBio);
                TextView artisanName = (TextView) findViewById(R.id.artisanName);
                System.out.print("bio: " + artisan.getBio());
                artisanBio.setText(artisan.getBio());
                artisanName.setText(artisan.getFirstName() + " " + artisan.getLastName());
                ListView artisanList = (ListView)findViewById(R.id.artisanProductList);
                if (artisan.getArtisanItems().size() > 0) {
                    ScrollingActivity.ArtisanProductAdapter artisanProductAdapter = new ScrollingActivity.ArtisanProductAdapter(artisan.getArtisanItems());
                    artisanList.setAdapter(artisanProductAdapter);
                }

            }

            @Override
            public void onFailure(Call<RestfulResponse<Artisan>> call, Throwable t) {
                Toast.makeText(ScrollingActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static ArtisanProductAdapter getAdapter(){
        return artisanProductAdapterGlobal;
    }

    class ArtisanProductAdapter extends BaseAdapter {

        List<ArtisanItem> artisanItems;

        public ArtisanProductAdapter(List<ArtisanItem> artisanItems) {

            this.artisanItems = artisanItems;
        }

        public int getCount() {
            return artisanItems.size();
        }
        public ArtisanItem getItem(int i) {
            return artisanItems.get(i);
        }
        public void addItem(ArtisanItem artisanItem) { artisanItems.add(artisanItem);}
        public long getItemId(int i) {
            return artisanItems.get(i).getItemId();
        }
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.artisan_products_layout, null);
            TextView itemName = (TextView)view.findViewById(R.id.itemName);
            TextView itemDescription = (TextView)view.findViewById(R.id.itemDescription);
            itemName.setText(artisanItems.get(i).getItemName());
            itemDescription.setText(artisanItems.get(i).getItemDescription());
            return view;
        }

    }
}
