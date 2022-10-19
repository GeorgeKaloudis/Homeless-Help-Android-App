package com.example.homelessapp.DTO;

public class User {
    private String username,firstname,lastname,email,userid;

    public User(String username, String firstname, String lastname, String email, String userid) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.userid = userid;
    }
    public User(){

    }

    public String getUserid() { return userid; }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

}


