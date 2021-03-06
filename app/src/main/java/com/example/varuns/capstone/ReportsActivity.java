package com.example.varuns.capstone;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.varuns.capstone.model.Artisan;
import com.example.varuns.capstone.model.Report;
import com.example.varuns.capstone.model.ReportDate;
import com.example.varuns.capstone.model.SoldItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReportsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ListView reportList;
    private ListView soldItemList;
    private Button backButton;
    private static ReportsActivity.ReportAdapter reportAdapterGlobal;

    Date startDate, endDate;
    AnyChartView anyChartView;


    List<SoldItem> currSoldItems = new LinkedList<>();
    private int year, month, day;
    Button startDateButton, endDateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        setTitle("Reports");

        reportList = (ListView) findViewById(R.id.reportList);
        soldItemList = (ListView) findViewById(R.id.soldItemList);
        backButton = findViewById(R.id.back_to_report);
        backButton.setVisibility(View.GONE);
        soldItemList.setVisibility(View.GONE);

        reportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                ReportDate reportDate = (ReportDate) parent.getAdapter().getItem(position);
                parent.setVisibility(View.GONE);
                backButton.setVisibility(View.VISIBLE);
                soldItemList.setVisibility(View.VISIBLE);
                ListView soldItemList = (ListView) findViewById(R.id.soldItemList);
                ReportsActivity.SoldItemAdapter soldItemAdapter = new ReportsActivity.SoldItemAdapter(reportDate);
                soldItemList.setAdapter(soldItemAdapter);
            }
        });

        // generate Dates
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 0);
        calendar.add(Calendar.DATE, -7);
        startDate = calendar.getTime();
        calendar.add(Calendar.DATE, 7);
        endDate = calendar.getTime();
      
// you can directly pass Date objects to DataPoint-Constructor
// this will convert the Date to double via Date#getTime()

        anyChartView = findViewById(R.id.any_chart_view);

        setupBottomNavigationView();

        List<Artisan> artisansList = menu_activity.getAdapter().getArtisans();

        //If the artisan was manually selected, then make sure it is selected within the spinner
        if (this.getIntent().getExtras() != null) {
            Integer artisanId = this.getIntent().getExtras().getInt("artisanId");
            for (Artisan a : artisansList) {
                if (a.getArtisanId() == artisanId) {
                    artisansList.remove(a);
                    artisansList.add(0, a);
                    break;
                }
            }
        }

        Spinner spinner = (Spinner) findViewById(R.id.artisans_spinner);
        // Create an ArrayAdapter using the artisans array and a default spinner layout
        ArrayAdapter<Artisan> adapter = new ArrayAdapter<Artisan>(this,
                android.R.layout.simple_spinner_item, artisansList);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //configure date picking stuff
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        startDateButton = findViewById(R.id.dateOne);
        endDateButton = findViewById(R.id.dateTwo);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 101) {
            return new DatePickerDialog(this,
                    startDateListener, year, month, day);
        }
        if (id == 102) {
            return new DatePickerDialog(this,
                    endDateListener, year, month, day);
        }

        return null;
    }

    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    private DatePickerDialog.OnDateSetListener startDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker picker, int year, int month, int day) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    startDate = calendar.getTime();
                    startDateButton.setText(formatter.format(startDate));
                    createGraphData(currSoldItems);
        }
    };

    private DatePickerDialog.OnDateSetListener endDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker picker, int year, int month, int day) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    endDate = calendar.getTime();
                    endDateButton.setText(formatter.format(endDate));
                    createGraphData(currSoldItems);
        }
    };

    public void setStartDate(View view) {
        showDialog(101);
    }

    public void setEndDate(View view) {
        showDialog(102);
    }

    private String convertDate(String dateToString) {
        String[] split = dateToString.split(" ");
        return split[1] + " " + split[2] + " " + split[5];
    }

    public void goBackToReport(View view) {
        soldItemList.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
        reportList.setVisibility(View.VISIBLE);
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

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        if (!(bottomNavigationView.getSelectedItemId() == R.id.navigation_notifications))
            bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);
    }

    private boolean verifySelectedDates() {
        if (startDate.getTime() < endDate.getTime()) {
            startDateButton.setError(null);
            endDateButton.setError(null);
            return true;
        }
        else {
            endDateButton.setError("End date must be past start!");
            startDateButton.setError("Start date must be before end!");
            return false;
        }
    }

    Column column;
    Cartesian cartesian;
    boolean graphCreated = false;
    public List<SoldItem> createGraphData(List<SoldItem> soldItems) {
        if (!verifySelectedDates()) {
            return new LinkedList<>();
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (soldItems == null) {
            soldItems = new LinkedList<>();
        }

        ReportsActivity.ReportAdapter reportAdapter = new ReportsActivity.ReportAdapter(soldItems);
        reportList.setAdapter(reportAdapter);
        reportAdapterGlobal = reportAdapter;

        List<DataEntry> data = new ArrayList<>();
        HashMap<Long, List<SoldItem>> map = new HashMap<>();

        for (SoldItem si : soldItems) {
            if (si.getDateSold().getTime() >= startDate.getTime()
                    && si.getDateSold().getTime() <= endDate.getTime()) {
                long dateTime = si.getDateSold().getTime();
                if (!map.containsKey(dateTime)) {
                    List<SoldItem> newItems = new LinkedList<SoldItem>();
                    newItems.add(si);
                    map.put(dateTime, newItems);
                }

                else {
                    map.get(dateTime).add(si);
                }
            }
        }

        data.add(new ValueDataEntry(convertDate(startDate.toString()),0));

        Long originalStartDate = startDate.getTime();
        while (startDate.getTime() < endDate.getTime()) {
            data.add(new ValueDataEntry(convertDate(startDate.toString()), 0));
            //increase by day
            startDate.setTime(startDate.getTime() + 86400000);
        }
        startDate = new Date(originalStartDate);

        int max = 0;
        for (Map.Entry<Long, List<SoldItem>> entry : map.entrySet()) {
            data.add(new ValueDataEntry(convertDate(new Date(entry.getKey()).toString()),
                    entry.getValue().size()));
            max = max >= entry.getValue().size() ? max : entry.getValue().size();
        }

        if (!graphCreated) {
            cartesian = AnyChart.column();
            cartesian.xScroller(true);
            cartesian.xZoom().setToPointsCount(100, false, cartesian.xScale());

            column = cartesian.column(data);
            column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d);

            cartesian.animation(true);
            cartesian.title("Products Sold");

            cartesian.yScale().minimum(0);

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian.interactivity().hoverMode(HoverMode.BY_X);

            cartesian.xAxis(0).title("Date");
            cartesian.yAxis(0).title("Products Sold");

            anyChartView.setChart(cartesian);
            graphCreated = true;
        }

        cartesian.yScale().maximum(max == 0 ? 1 : max);

        column.data(data);

        return soldItems;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        System.out.println("On Item Selected");
        Artisan current = (Artisan)parent.getItemAtPosition(pos);

        List<SoldItem> soldItems = current.getSoldItems();
        currSoldItems = soldItems;
        if (!(soldItems == null) && !soldItems.isEmpty())
            createGraphData(soldItems);
        else {
            soldItems = new ArrayList<SoldItem>();
            createGraphData(soldItems);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private class SoldItemAdapter extends BaseAdapter {

        private ReportDate reportDate;

        public SoldItemAdapter(ReportDate reportDate) {
            this.reportDate = reportDate;
        }

        public void addSoldItem(SoldItem si) {
            reportDate.addSoldItem(si);
        }

        public List<SoldItem> getSoldItems() {
            return reportDate.getSoldItems();
        }

        public int getCount() {
            return reportDate.getSoldItems().size();
        }

        public SoldItem getItem(int i) {
            return reportDate.getSoldItems().get(i);
        }

        public long getItemId(int i) {
            return 0;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.report_date_layout, null);


            TextView soldItemDescTextView = (TextView) view.findViewById(R.id.soldItemDescription);
            TextView soldItemPriceTextView = (TextView) view.findViewById(R.id.soldItemCost);
            soldItemDescTextView.setText(reportDate.getSoldItems().get(i).getItemDescription());
            soldItemPriceTextView.setText("Price: " + reportDate.getSoldItems().get(i).getPrice().toString());



            return view;
        }
    }

    private class ReportAdapter extends BaseAdapter {

        private Report report;

        public ReportAdapter(List<SoldItem> soldItems) {
            report = new Report();
            report.addSoldItems(soldItems);
        }

        public void addSoldItem(SoldItem si) {
            report.addSoldItem(si);
        }

        public Collection<ReportDate> getReportDates() {
            return report.getReportDateMap().values();
        }

        public int getCount() {
            return report.getReportDateMap().size();
        }

        public ReportDate getItem(int i) {
            List<Long> dateTimes = new ArrayList<>(report.getReportDateMap().keySet());
            Collections.sort(dateTimes);
            return report.getReportDateMap().get(dateTimes.get(i));
        }

        public long getItemId(int i) {
            return 0;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.sold_item_report_layout, null);
            List<Long> dateTimes = new ArrayList<>(report.getReportDateMap().keySet());
            Collections.sort(dateTimes);

            TextView reportDate = (TextView) view.findViewById(R.id.reportDate);
            Date reportD = new Date(dateTimes.get(i));
            reportDate.setText(reportD.toString());


            return view;
        }
    }
}
