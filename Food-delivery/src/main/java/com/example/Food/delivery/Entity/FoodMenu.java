package com.example.Food.delivery.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "menu")
public class FoodMenu {

    @Id
    private Long id;  // Same as User.id, shared PK

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId  // This tells JPA to use the User's id as PK here
    @JoinColumn(name = "id")  // FK column in menu table referencing users(id)
    private User vendor;

    @Column(name = "m_name", nullable = false)
    private String mName;

    private String description;

    private BigDecimal price;

    private String availability;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getVendor() {
        return vendor;
    }

    public void setVendor(User vendor) {
        this.vendor = vendor;
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
