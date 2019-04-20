package com.example.keypermobile.models;

public class Password {

    private int id;
    private String title;
    private String website;
    private int image;

    public Password(int id, String title, String website, int image) {
        this.id = id;
        this.title = title;
        this.website = website;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getWebsite() {
        return website;
    }

    public int getImage() {
        return image;
    }
}
