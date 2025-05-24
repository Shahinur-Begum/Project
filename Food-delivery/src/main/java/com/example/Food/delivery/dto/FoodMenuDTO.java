package com.example.Food.delivery.dto;

import java.math.BigDecimal;

public class FoodMenuDTO {
    private Long mId;           // Changed from Integer to Long
    private Long vendorId;      // Matches User.id type
    private String mName;
    private String description;
    private BigDecimal price;
    private String availability;

    // Getters and setters
    public Long getMId() {
        return mId;
    }

    public void setMId(Long mId) {
        this.mId = mId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getMName() {
        return mName;
    }

    public void setMName(String mName) {
        this.mName = mName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
