package com.example.phasmatic.data.model;

public class Note {
    private String id;
    private String title;
    private String description;
    private long createdTime;
    private String user_id;

    public Note() {
    }

    public Note(String id, String title, String description, long createdTime, String user_id) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdTime = createdTime;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}