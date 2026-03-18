package ru.fa.carrental.model;

import jakarta.persistence.*;

/**
 * Entity describing a car available for rent.
 *
 * <p>Используется JPA для хранения в таблице {@code cars}.</p>
 */
@Entity
@Table(name = "cars")
public class Car {
    /** Primary key (auto generated). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Model name (e.g. "Midnight Marauder"). */
    private String model;

    /** Category or type (e.g. "Premium", "SUV"). */
    private String type;

    /** Category (parent entity). */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CarCategory category;

    /** Year of manufacture. */
    private int year;

    /** Rental price per day in local currency. */
    private double pricePerDay;

    /** Availability status. */
    private boolean available;

    public Car() {
    }

    public Car(String model, String type, int year, double pricePerDay, boolean available) {
        this.model = model;
        this.type = type;
        this.year = year;
        this.pricePerDay = pricePerDay;
        this.available = available;
    }

    public Car(String model, String type, CarCategory category, int year, double pricePerDay, boolean available) {
        this(model, type, year, pricePerDay, available);
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CarCategory getCategory() {
        return category;
    }

    public void setCategory(CarCategory category) {
        this.category = category;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
