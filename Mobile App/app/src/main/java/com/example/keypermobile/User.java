package com.example.keypermobile;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class User
{
    String username;
    String email;
    String password;
    ArrayList<Site> sites;

    public User(String username, String email, String password, ArrayList<Site> sites)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.sites = sites;
    }

    public class Site
    {
        String title;
        String username;
        String email;
        String password;
        String url;
        public ArrayList<String> tags;
        String notes;
        LocalDateTime dateAdded;
        LocalDateTime dateLastUsed;
        String id;

        // put serialization and deserialization in site use constructor to deserialize
        // padding to mult of 32 when serializing , unpad when deserializing 
        public Site(String title, String username, String email, String password, String url,
                    ArrayList<String> tags, String notes,  LocalDateTime dateAdded, LocalDateTime dateLastUsed)
        {
            this.title = title;
            this.username = username;
            this.email = email;
            this.password = password;
            this.url = url;
            this.tags = tags;
            this.notes = notes;
            this.dateAdded = dateAdded;
            this.dateLastUsed = dateLastUsed;
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

        public LocalDateTime getDateAdded() {
            return dateAdded;
        }

        public void setDateAdded(LocalDateTime dateAdded) {
            this.dateAdded = dateAdded;
        }

        public LocalDateTime getDateLastUsed() {
            return dateLastUsed;
        }

        public void setDateLastUsed(LocalDateTime dateLastUsed) {
            this.dateLastUsed = dateLastUsed;
        }
    }
}
