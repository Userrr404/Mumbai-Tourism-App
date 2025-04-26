package com.example.tourismapp;

public class Place {
    private String id;
    private String imageUrl;
    private String name;
    private String description;
    private String category;
    private String tags;
    private String exactLocation;
    private String timing;
    private String fees;
    private String contact;
    private String location;

//    public Place(String id, String name, String imageUrl) {
//        this.id = id;
//        this.name = name;
//        this.imageUrl = imageUrl;
////        this.description = description;
//    }

    // Constructor
    public Place(String id, String imageUrl, String name, String description, String category,
                        String tags, String exactLocation, String timing, String fees, String contact, String location) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.category = category;
        this.tags = tags;
        this.exactLocation = exactLocation;
        this.timing = timing;
        this.fees = fees;
        this.contact = contact;
        this.location = location;
    }

    // Empty constructor (optional, but useful for Firebase, parsing, etc.)
    public Place() {}

    // Getters and Setters
    public String getPlaceId() {
        return id;
    }

    public void setPlaceId(String id) {
        this.id = id;
    }

    public String getImagePath() {
        return imageUrl;
    }

    public void setImagePath(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getExactLocation() {
        return exactLocation;
    }

    public void setExactLocation(String exactLocation) {
        this.exactLocation = exactLocation;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
