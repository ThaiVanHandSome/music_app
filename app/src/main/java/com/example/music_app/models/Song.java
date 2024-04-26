package com.example.music_app.models;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.List;

public class Song {
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
    @SerializedName("playlistSongs")
    private List<PlaylistSong> playlistSongs;
    @SerializedName("songLikeds")
    private List<SongLiked> songLikeds;
    @SerializedName("songComments")
    private List<SongComment> songComments;

    public Long getIdSong() {
        return idSong;
    }

    public void setIdSong(Long idSong) {
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

    public LocalDateTime getDayCreated() {
        LocalDateTime dateTime = LocalDateTime.of(
                dayCreated.get(0),
                dayCreated.get(1),
                dayCreated.get(2),
                dayCreated.get(3),
                dayCreated.get(4),
                dayCreated.get(5));
        return dateTime;
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

    public List<PlaylistSong> getPlaylistSongs() {
        return playlistSongs;
    }

    public void setPlaylistSongs(List<PlaylistSong> playlistSongs) {
        this.playlistSongs = playlistSongs;
    }

    public List<SongLiked> getSongLikeds() {
        return songLikeds;
    }

    public void setSongLikeds(List<SongLiked> songLikeds) {
        this.songLikeds = songLikeds;
    }

    public List<SongComment> getSongComments() {
        return songComments;
    }

    public void setSongComments(List<SongComment> songComments) {
        this.songComments = songComments;
    }
}
