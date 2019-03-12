package com.example.varuns.capstone.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Report {

    private HashMap<Long, ReportDate> reportDateMap;

    public Report() {
        reportDateMap = new HashMap<>();
    }

    public void addSoldItem(SoldItem soldItem) {
        Date d = new Date(0);
        d.setYear(soldItem.getDateSold().getYear());
        d.setMonth(soldItem.getDateSold().getMonth());
        d.setDate(soldItem.getDateSold().getDate());
        if (reportDateMap.containsKey(d.getTime())) {
            ReportDate rd = reportDateMap.get(d.getTime());
            rd.addSoldItem(soldItem);
            reportDateMap.put(d.getTime(), rd);
        } else {
            ReportDate reportDate = new ReportDate();
            reportDate.setDateTime(d.getTime());
            reportDate.addSoldItem(soldItem);
            reportDateMap.put(d.getTime(), reportDate);
        }
    }

    public void addSoldItems(List<SoldItem> soldItems) {
        for (SoldItem s : soldItems) {
            addSoldItem(s);
        }
    }

    public HashMap<Long, ReportDate> getReportDateMap() {
        return reportDateMap;
    }

    public void setReportDateMap(HashMap<Long, ReportDate> reportDateMap) {
        this.reportDateMap = reportDateMap;
    }
}
