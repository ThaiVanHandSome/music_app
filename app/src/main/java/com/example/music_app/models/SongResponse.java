package com.example.music_app.models;

<<<<<<< HEAD
import java.util.List;

public class SongResponse {
    private List<Song> content;
    private int totalElements;
    private int totalPages;
    private boolean last;

    public List<Song> getContent() {
        return content;
    }

    public void setContent(List<Song> content) {
        this.content = content;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
=======
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SongResponse {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("error")
    private Boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<Song> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Song> getData() {
        return data;
    }

    public void setData(List<Song> data) {
        this.data = data;
>>>>>>> b04fcfb10aa465867953c8b1b63fe7e24b27494d
    }
}
