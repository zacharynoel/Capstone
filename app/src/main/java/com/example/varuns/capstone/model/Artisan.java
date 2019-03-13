package com.example.varuns.capstone.model;

import java.util.List;

public class Artisan {

    Integer artisanId;
    String firstName;
    String lastName;
    String bio;
    String phoneNo;
    List<ArtisanItem> artisanItems;
    String phoneNo;

    public Artisan(Integer artisanId, String firstName, String lastName, String bio, List<ArtisanItem> artisanItems, String phoneNo) {
        this.artisanItems = artisanItems;
        this.artisanId = artisanId;
        this.firstName = firstName;
        this.bio = bio;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
    }

    public Artisan() {
    }

    public Integer getArtisanId() {
        return artisanId;
    }

    public void setArtisanId(Integer artisanId) {
        this.artisanId = artisanId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<ArtisanItem> getArtisanItems() {
        return artisanItems;
    }

    public void setArtisanItems(List<ArtisanItem> artisanItems) {
        this.artisanItems = artisanItems;
    }

    public String getPhoneNo(){ return phoneNo; }

    public void setPhoneNo(String phoneNo){ this.phoneNo = phoneNo; }

    public String toString() {
        return firstName + " " + lastName;
    }
  
    public String getPhoneNo() {
        return phoneNo;
    }
}
