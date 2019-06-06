package com.example.varuns.capstone;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.varuns.capstone.Util.ImageUtil;
import com.example.varuns.capstone.model.Artisan;
import com.example.varuns.capstone.model.ArtisanItem;
import com.example.varuns.capstone.services.ApiService;
import com.example.varuns.capstone.services.RestfulResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BioFragment extends Fragment {

    int position;
    Artisan artisan;
    int artisanId;
    TextView bioTextView;
    RecyclerView artisanList;
    ImageView productImage;
    TextView productName;
    ImageView productImage1;
    TextView productName1;
    ImageView productImage2;
    TextView productName2;

    public static Fragment getInstance(int position, int artisanId) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        BioFragment bioFragment = new BioFragment();
        bioFragment.setArguments(bundle);
        bioFragment.setArtisanId(artisanId);
        return bioFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
    }

    @Override
    public void onResume() {
        super.onResume();
        getArtisanById(artisanId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getArtisanById(artisanId);
        bioTextView = (TextView) view.findViewById(R.id.bioTextView);
        productName = (TextView) view.findViewById(R.id.productName);
        productImage = (ImageView) view.findViewById(R.id.productImage);
        productName1 = (TextView) view.findViewById(R.id.productName1);
        productImage1 = (ImageView) view.findViewById(R.id.productImage1);
        productName2 = (TextView) view.findViewById(R.id.productName2);
        productImage2 = (ImageView) view.findViewById(R.id.productImage2);


    }

    private void getArtisanById(final Integer artisanId) {

        Call<RestfulResponse<Artisan>> call = ApiService.artisanService().getArtisanById(artisanId.toString());
        //handle the response
        call.enqueue(new Callback<RestfulResponse<Artisan>>() {
            @Override
            public void onResponse(Call<RestfulResponse<Artisan>> call, Response<RestfulResponse<Artisan>> response) {
                artisan = response.body().getData();
                bioTextView.setText(artisan.getBio());
                setBestSellers(artisan);

            }
            @Override
            public void onFailure(Call<RestfulResponse<Artisan>> call, Throwable t) {
            }
        });

    }
    public void setBestSellers(Artisan artisan)  {
        if (artisan.getArtisanItems().size() > 0) {
            if (artisan.getArtisanItems().get(0) != null) {
                if (artisan.getArtisanItems().get(0).getEncodedImage() != null) {
                    Bitmap decodedByte = ImageUtil.encodedStringToBitmap(artisan.getArtisanItems().get(0).getEncodedImage());
                    productImage.setImageBitmap(decodedByte);
                }
                productName.setText(artisan.getArtisanItems().get(0).getItemName());
            }
            if (artisan.getArtisanItems().size() > 1) {
                if (artisan.getArtisanItems().get(1).getEncodedImage() != null) {
                    Bitmap decodedByte = ImageUtil.encodedStringToBitmap(artisan.getArtisanItems().get(1).getEncodedImage());
                    productImage1.setImageBitmap(decodedByte);
                }
                productName1.setText(artisan.getArtisanItems().get(1).getItemName());
            }
            if (artisan.getArtisanItems().size() > 2) {
                if (artisan.getArtisanItems().get(2).getEncodedImage() != null) {
                    Bitmap decodedByte = ImageUtil.encodedStringToBitmap(artisan.getArtisanItems().get(2).getEncodedImage());
                    productImage2.setImageBitmap(decodedByte);
                }
                productName2.setText(artisan.getArtisanItems().get(2).getItemName());
            }
        }
    }

    public int getArtisanId() {
        return artisanId;
    }

    public void setArtisanId(int artisanId) {
        this.artisanId = artisanId;
    }
}