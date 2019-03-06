package com.example.varuns.capstone.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class SoldItem {

        private Integer soldItemId;
        private Timestamp dateSold;
        private Integer ArtisanId;
        private Integer itemId;
        private BigDecimal price;
        private String itemDescription;

        public Integer getSoldItemId() {
            return soldItemId;
        }

        public void setSoldItemId(Integer soldItemId) {
            this.soldItemId = soldItemId;
        }

        public Timestamp getDateSold() {
            return dateSold;
        }

        public void setDateSold(Timestamp dateSold) {
            this.dateSold = dateSold;
        }

        public Integer getArtisanId() {
            return ArtisanId;
        }

        public void setArtisanId(Integer artisanId) {
            ArtisanId = artisanId;
        }

        public Integer getItemId() {
            return itemId;
        }

        public void setItemId(Integer itemId) {
            this.itemId = itemId;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getItemDescription() {
            return itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }
    }

}
