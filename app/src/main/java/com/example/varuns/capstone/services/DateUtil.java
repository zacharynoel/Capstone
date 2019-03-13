package com.example.varuns.capstone.services;

import com.example.varuns.capstone.model.SoldItem;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DateUtil {

    public static DataPoint[] getDataPointsFromDates(List<Long> dates) {
        HashMap<Long, Integer> soldByDayMap = new HashMap<>();
        System.out.println("Size: " + dates.size());
        for (Long date : dates) {
            Date actualDate = new Date(date);
            Date d = new Date(0);
            d.setYear(actualDate.getYear());
            d.setMonth(actualDate.getMonth());
            d.setDate(actualDate.getDate());
            if (soldByDayMap.containsKey(d.getTime())) {
                soldByDayMap.put(d.getTime(), soldByDayMap.get(d.getTime()) + 1);
            } else {
                soldByDayMap.put(d.getTime(), 1);
            }
        }
        List<Long> timeKeys = new ArrayList<>(soldByDayMap.keySet());

        Collections.sort(timeKeys);

        List<DataPoint> dataPoints = new ArrayList<>();
        System.out.println("keyset Length: " + soldByDayMap.keySet().size());

        for (Long key : timeKeys) {
            Date d = new Date(key);
            dataPoints.add(new DataPoint(d, soldByDayMap.get(key)));
        }

        DataPoint[] dataPointsArr = new DataPoint[dataPoints.size()];
        for (int i = 0; i < dataPoints.size(); i++) {
            dataPointsArr[i] = dataPoints.get(i);
        }
        for (Long l : soldByDayMap.keySet()) {
            Date dd = new Date(l);
            System.out.println(dd.toString() + " : " + soldByDayMap.get(l));
        }
        return dataPointsArr;
    }

}
