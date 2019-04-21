package com.example.keypermobile.models;

import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Site
{
    String title;
    String username;
    String email;
    String password;
    String url;
    public ArrayList<String> tags;
    String notes;
    private String id;

    // put serialization and deserialization in site use constructor to deserialize
    // padding to mult of 32 when serializing , unpad when deserializing
    public Site(String title, String username, String email, String password, String url,
                ArrayList<String> tags, String notes)
    {
        this.title = title;
        this.username = username;
        this.email = email;
        this.password = password;
        this.url = url;
        this.tags = tags;
        this.notes = notes;
    }

    public Site(String json)
    {
        Gson gson = new Gson();
        gson.fromJson(json, Site.class);
        Site s = gson.fromJson(json, Site.class);
        this.title = s.title;
        this.username = s.username;
        this.email = s.email;
        this.password = s.password;
        this.url = s.url;
        this.tags = s.tags;
        this.notes = s.notes;
        this.id = s.id;
    }

    public String toJson()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
