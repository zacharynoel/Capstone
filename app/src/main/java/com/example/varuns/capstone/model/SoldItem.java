package com.example.varuns.capstone.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class SoldItem {

        private Integer soldItemId;
        private Date dateSold;
        private Integer artisanId;
        private Integer itemId;
        private BigDecimal price;
        private String itemDescription;

        public Integer getSoldItemId() {
            return soldItemId;
        }

        public void setSoldItemId(Integer soldItemId) {
            this.soldItemId = soldItemId;
        }

        public Date getDateSold() {
            return dateSold;
        }

        public void setDateSold(Date dateSold) {
            this.dateSold = dateSold;
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

