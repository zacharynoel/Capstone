package com.example.varuns.capstone.model;

public class ArtisanItem {

    Integer artisanId;
    Integer itemId;
    String itemName;
    String itemDescription;

    public ArtisanItem(Integer artisanId, Integer itemId, String itemName, String itemDescription) {
        this.artisanId = artisanId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
    }

    public Integer getArtisanId() {
        return artisanId;
    }

    public void setArtisanId(Integer artisanId) {
        this.artisanId = artisanId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}
