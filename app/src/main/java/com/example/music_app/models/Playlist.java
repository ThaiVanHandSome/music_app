package com.example.music_app.models;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.List;

public class Playlist {
    @SerializedName("idPlaylist")
    private Long idPlaylist;
    @SerializedName("name")
    private String name;
    @SerializedName("dayCreated")
    private List<Integer> dayCreated;
    @SerializedName("image")
    private String image;
    @SerializedName("playlistSongs")
    private List<PlaylistSong> playlistSongs;

    public Playlist() {
    }

    public Playlist(Long idPlaylist, String name, List<Integer> dayCreated, String image, List<PlaylistSong> playlistSongs) {
        this.idPlaylist = idPlaylist;
        this.name = name;
        this.dayCreated = dayCreated;
        this.image = image;
        this.playlistSongs = playlistSongs;
    }

    public Long getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(Long idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
