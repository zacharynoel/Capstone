package com.example.varuns.capstone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.varuns.capstone.model.Artisan;
import com.example.varuns.capstone.model.SoldItem;
import com.example.varuns.capstone.services.ApiService;
import com.example.varuns.capstone.services.RestfulResponse;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        // generate Dates
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d4 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d5 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d6 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d7 = calendar.getTime();


        GraphView graph = (GraphView) findViewById(R.id.graph);

// you can directly pass Date objects to DataPoint-Constructor
// this will convert the Date to double via Date#getTime()
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3),
                new DataPoint(d4, 1),
                new DataPoint(d5, 5),
                new DataPoint(d6, 3),
                new DataPoint(d7, 6)
        });

        graph.addSeries(series);

        graph.setTitle("Products Sold");

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);

        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d7.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getGridLabelRenderer().setNumVerticalLabels(7);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(6);
        graph.getViewport().setYAxisBoundsManual(true);

        graph.getGridLabelRenderer().setHumanRounding(false);

        setupBottomNavigationView();

        /*Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.values.fake_artisans, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);*/

        //getArtisans();
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent = new Intent(ReportsActivity.this, menu_activity.class);
                        startActivity(intent);
                        break;

                    case R.id.navigation_dashboard:
                        Intent intent1 = new Intent(ReportsActivity.this, Send_message.class);
                        startActivity(intent1);
                        break;

                    case R.id.navigation_notifications:
                        Intent intent2 = new Intent(ReportsActivity.this, ReportsActivity.class);
                        startActivity(intent2);
                        break;

                }
                return true;
            }
        });
    }

    public void getArtisans() {
        Call<RestfulResponse<List<SoldItem>>> call = ApiService.itemService().getSoldItemsByUserId("1");
        //handle the response
        call.enqueue(new Callback<RestfulResponse<List<SoldItem>>>() {
            @Override
            public void onResponse(Call<RestfulResponse<List<SoldItem>>> call, Response<RestfulResponse<List<SoldItem>>> response) {
                List<SoldItem> soldItems = response.body().getData();
                System.out.println(soldItems.size());
                //for (SoldItem i : soldItems) {
                    //System.out.println("item");
                //}
                Toast.makeText(ReportsActivity.this, "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RestfulResponse<List<SoldItem>>> call, Throwable t) {
                Toast.makeText(ReportsActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
