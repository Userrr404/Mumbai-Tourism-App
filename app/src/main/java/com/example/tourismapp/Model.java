package com.example.tourismapp;

public class Model {
    private String id,image,name,description, category, tags, exact_location, timing, fees, contact,latitude,longitude;
    private boolean isSaved;
    public Model(String id, String image, String name, String description,String category,String tags, String exact_location, String timing,String fees, String contact,String latitude,String longitude){
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.category = category;
        this.tags = tags;
        this.exact_location = exact_location;
        this.timing = timing;
        this.fees = fees;
        this.contact = contact;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getExact_location() {
        return exact_location;
    }
    public void setExact_location(String exact_location) {
        this.exact_location = exact_location;
    }
    public String getFees() {
        return fees;
    }
    public void setFees(String fees) {
        this.fees = fees;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public String getTiming() {
        return timing;
    }
    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean isSaved() {
        return isSaved;
    }
    public void setSaved(boolean saved) {
        isSaved = saved;
    }
}
