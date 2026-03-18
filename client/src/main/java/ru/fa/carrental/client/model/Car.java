package ru.fa.carrental.client.model;

import javafx.beans.property.*;

public class Car {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty model = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final ObjectProperty<CarCategory> category = new SimpleObjectProperty<>();
    private final IntegerProperty year = new SimpleIntegerProperty();
    private final DoubleProperty pricePerDay = new SimpleDoubleProperty();
    private final BooleanProperty available = new SimpleBooleanProperty();

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getModel() {
        return model.get();
    }

    public StringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public CarCategory getCategory() {
        return category.get();
    }

    public ObjectProperty<CarCategory> categoryProperty() {
        return category;
    }

    public void setCategory(CarCategory category) {
        this.category.set(category);
    }

    public int getYear() {
        return year.get();
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public double getPricePerDay() {
        return pricePerDay.get();
    }

    public DoubleProperty pricePerDayProperty() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay.set(pricePerDay);
    }

    public boolean isAvailable() {
        return available.get();
    }

    public BooleanProperty availableProperty() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available.set(available);
    }
}
