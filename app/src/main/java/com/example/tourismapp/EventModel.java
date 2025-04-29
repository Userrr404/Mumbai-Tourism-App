package com.example.tourismapp;

public class EventModel {
    String id, image, name, date, description;

    public EventModel(String id, String image, String name, String date, String description) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.date = date;
        this.description = description;
    }

    // Getters
    public String getId() { return id; }
    public String getImage() { return image; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
}
