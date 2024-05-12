package com.example.music_app.models;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Song {
    private int idSong;
    private String name;
    private int views;
    private List<Integer> dayCreated;
    private String resource;
    private String image;

    public int getIdSong() {
        return idSong;
    }

    public void setIdSong(int idSong) {
        this.idSong = idSong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public List<Integer> getDayCreated() {
        return dayCreated;
    }

    public void setDayCreated(List<Integer> dayCreated) {
        this.dayCreated = dayCreated;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
