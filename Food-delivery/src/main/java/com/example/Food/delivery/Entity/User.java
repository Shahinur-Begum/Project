package com.example.Food.delivery.Entity;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // primary key column
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password") // nullable for Google users
    private String password;

    @Column(name = "google_id", unique = true)
    private String googleId; // nullable for normal users

    @Column(name = "role", nullable = false)
    private String role;  // "student" or "vendor"

    // Vendor-specific fields, nullable for students
    @Column(name = "availability_status")
    private String availabilityStatus; // e.g. "ON" or "OFF"

    @Column(name = "schedule")
    private String schedule; // vendor schedule info as string

    // Getters and setters (or use Lombok @Getter/@Setter)
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}

