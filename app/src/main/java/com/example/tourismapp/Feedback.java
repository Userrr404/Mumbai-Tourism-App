package com.example.tourismapp;

public class Feedback {
    private int id;
    private String username;
    private String user_feed;
    private String created_At;

    public Feedback(int id, String username, String user_feed, String created_At) {
        this.id = id;
        this.username = username;
        this.user_feed = user_feed;
        this.created_At = created_At;
    }

    public Feedback() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_feed() {
        return user_feed;
    }

    public void setUser_feed(String user_feed) {
        this.user_feed = user_feed;
    }

    public String getCreated_At() {
        return created_At;
    }

    public void setCreated_At(String created_At) {
        this.created_At = created_At;
    }
}
