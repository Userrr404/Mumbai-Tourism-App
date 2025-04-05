package com.example.tourismapp;

public class Model {
    private String id,image,name,description, category, tags, exact_location, timing, fees, contact;

    public Model(String id, String image, String name, String description,String category,String tags, String exact_location, String timing,String fees, String contact){
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

    //    private String id,image,name,info;
//
//    public Model(String id, String image, String name, String info){
//        this.id = id;
//        this.image = image;
//        this.name = name;
//        this.info = info;
//    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }
//
//    public String getInfo() {
//        return info;
//    }
//
//    public void setInfo(String info) {
//        this.info = info;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
}
