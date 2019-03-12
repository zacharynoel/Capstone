package com.example.varuns.capstone.model;

import java.util.ArrayList;
import java.util.List;

public class ReportDate {

    private Long dateTime;
    private List<SoldItem> soldItems;

    public ReportDate() {
        soldItems = new ArrayList<>();

    }
    public ReportDate(List<SoldItem> soldItems) {
        this.soldItems = soldItems;
    }

    public void addSoldItem(SoldItem soldItem) {
        soldItems.add(soldItem);
    }
    public SoldItem getSoldItem(Integer index) {
        return soldItems.get(index);
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public List<SoldItem> getSoldItems() {
        return soldItems;
    }

    public void setSoldItems(List<SoldItem> soldItems) {
        this.soldItems = soldItems;
    }
}
