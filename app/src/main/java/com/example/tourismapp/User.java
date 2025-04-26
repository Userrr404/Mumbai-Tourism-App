package com.example.tourismapp;

public class User {
    private String username;
    private String email;
    private String password;
    private String full_name;

    public User(String username, String email,String password, String full_name) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.full_name = full_name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getFull_name(){
        return full_name;
    }
}
