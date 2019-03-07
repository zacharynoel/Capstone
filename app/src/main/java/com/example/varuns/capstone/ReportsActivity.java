package com.example.varuns.capstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.varuns.capstone.model.Artisan;
import com.example.varuns.capstone.model.SoldItem;
import com.example.varuns.capstone.services.ApiService;
import com.example.varuns.capstone.services.RestfulResponse;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);
    }


    public void getArtisans() {
        Call<RestfulResponse<List<SoldItem>>> call = ApiService.itemService().getSoldItemsByUserId("1");
        //handle the response
        call.enqueue(new Callback<RestfulResponse<List<SoldItem>>>() {
            @Override
            public void onResponse(Call<RestfulResponse<List<SoldItem>>> call, Response<RestfulResponse<List<SoldItem>>> response) {
                List<SoldItem> soldItems = response.body().getData();
                Toast.makeText(ReportsActivity.this, "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RestfulResponse<List<SoldItem>>> call, Throwable t) {
                Toast.makeText(ReportsActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
