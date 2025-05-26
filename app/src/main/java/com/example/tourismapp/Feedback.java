package com.example.tourismapp;

public class Feedback {
    private int id;
    private int user_id;
    private String username;
    private String user_feed;
    private String created_At;

    public Feedback(int id,int user_id, String username, String user_feed, String created_At) {
        this.id = id;
        this.user_id = id;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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
