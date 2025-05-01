package com.example.tourismapp;

public class Booking {
    int id;
    int place_id;
    String image_path;
    String name;
    String description;
    int user_id;
    String email;
    int number_of_people;
    String booking_date;
    int fees;
    String full_name;

    String mobile_number;
    String booking_status;

    public Booking(int id, int place_id,String image_path, String name, String description, int user_id, String email, int number_of_people, String booking_date, int fees, String full_name, String mobile_number){
        this.id = id;
        this.place_id = place_id;
        this.image_path = image_path;
        this.name = name;
        this.description = description;
        this.user_id = user_id;
        this.email = email;
        this.number_of_people = number_of_people;
        this.booking_date = booking_date;
        this.fees = fees;
        this.full_name = full_name;
        this.mobile_number = mobile_number;
    }

    public Booking() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumber_of_people() {
        return number_of_people;
    }

    public void setNumber_of_people(int number_of_people) {
        this.number_of_people = number_of_people;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public void getFull_name(String full_name){
        this.full_name = full_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }
}
