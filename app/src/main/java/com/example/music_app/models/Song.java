package com.example.music_app.models;

<<<<<<< HEAD
import androidx.annotation.Nullable;

import java.time.LocalDate;
=======
import com.google.gson.annotations.SerializedName;

>>>>>>> b04fcfb10aa465867953c8b1b63fe7e24b27494d
import java.time.LocalDateTime;
import java.util.List;

public class Song {
<<<<<<< HEAD
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
=======
    @SerializedName("idSong")
    private Long idSong;
    @SerializedName("name")
    private String name;
    @SerializedName("views")
    private int views;
    @SerializedName("dayCreated")
    private List<Integer> dayCreated;
    @SerializedName("resource")
    private String resource;
    @SerializedName("image")
    private String image;
    @SerializedName("artistId")
    private Long artistId;
    @SerializedName("artistName")
    private String artistName;

    public Song() {
    }

    public Song(Long idSong, String name, int views, List<Integer> dayCreated, String resource, String image, Long artistId, String artistName) {
        this.idSong = idSong;
        this.name = name;
        this.views = views;
        this.dayCreated = dayCreated;
        this.resource = resource;
        this.image = image;
        this.artistId = artistId;
        this.artistName = artistName;
    }

    public Long getIdSong() {
        return idSong;
    }

    public void setIdSong(Long idSong) {
>>>>>>> b04fcfb10aa465867953c8b1b63fe7e24b27494d
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

<<<<<<< HEAD
    public List<Integer> getDayCreated() {
        return dayCreated;
=======
    public LocalDateTime getDayCreated() {
        LocalDateTime dateTime = LocalDateTime.of(
                dayCreated.get(0),
                dayCreated.get(1),
                dayCreated.get(2),
                dayCreated.get(3),
                dayCreated.get(4),
                dayCreated.get(5));
        return dateTime;
>>>>>>> b04fcfb10aa465867953c8b1b63fe7e24b27494d
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

<<<<<<< HEAD
=======
    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

>>>>>>> b04fcfb10aa465867953c8b1b63fe7e24b27494d
}
