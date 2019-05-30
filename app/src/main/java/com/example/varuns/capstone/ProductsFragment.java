package com.example.varuns.capstone;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varuns.capstone.Util.ImageUtil;
import com.example.varuns.capstone.model.Artisan;
import com.example.varuns.capstone.model.ArtisanItem;
import com.example.varuns.capstone.services.ApiService;
import com.example.varuns.capstone.services.RestfulResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsFragment extends Fragment {


    int position;
    private TextView textView;
    Artisan artisan;
    ListView artisanList;
    int artisanId;

    public static Fragment getInstance(int position, int artisanId) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        ProductsFragment productsFragment = new ProductsFragment();
        productsFragment.setArguments(bundle);
        productsFragment.setArtisanId(artisanId);
        return productsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        artisanList = (ListView)view.findViewById(R.id.artisanProductList);
        getArtisanById(artisanId);

    }

    private void getArtisanById(final Integer artisanId) {

        Call<RestfulResponse<Artisan>> call = ApiService.artisanService().getArtisanById(artisanId.toString());
        //handle the response
        call.enqueue(new Callback<RestfulResponse<Artisan>>() {
            @Override
            public void onResponse(Call<RestfulResponse<Artisan>> call, Response<RestfulResponse<Artisan>> response) {
                artisan = response.body().getData();
                if (artisan.getArtisanItems().size() > 0) {
                    ProductsFragment.ArtisanProductAdapter artisanProductAdapter = new ProductsFragment.ArtisanProductAdapter(artisan.getArtisanItems());
                    artisanList.setAdapter(artisanProductAdapter);
                }
            }
            @Override
            public void onFailure(Call<RestfulResponse<Artisan>> call, Throwable t) {
            }
        });

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
            ImageView imageView2 = (ImageView)view.findViewById(R.id.imageView2);
            if (artisanItems.get(i).getEncodedImage() != null) {
                Bitmap decodedByte = ImageUtil.encodedStringToBitmap(artisanItems.get(i).getEncodedImage());
                imageView2.setImageBitmap(decodedByte);
            }
            itemName.setText(artisanItems.get(i).getItemName() + " - " + artisanItems.get(i).getPrice().toString());
            itemDescription.setText(artisanItems.get(i).getItemDescription());
            return view;
        }

    }

    public int getArtisanId() {
        return artisanId;
    }

    public void setArtisanId(int artisanId) {
        this.artisanId = artisanId;
    }
}
