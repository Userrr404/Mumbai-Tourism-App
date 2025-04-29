package com.example.tourismapp;

public class Itinerary {
    private String name;
    private String imagePath;
    private String bookingDate;
    private int numberOfPeople;

    public Itinerary(String name, String imagePath, String bookingDate, int numberOfPeople) {
        this.name = name;
        this.imagePath = imagePath;
        this.bookingDate = bookingDate;
        this.numberOfPeople = numberOfPeople;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    // Setter methods (optional, if needed)
    public void setName(String name) {
        this.name = name;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
