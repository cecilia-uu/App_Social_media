package edu.northeastern.memecho.models;

import java.io.Serializable;

public class User implements Serializable {
    public String name;
    public String image;
    public String email;
    public String token;

    public User(String name, String image, String email, String token) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.token = token;
    }
}
