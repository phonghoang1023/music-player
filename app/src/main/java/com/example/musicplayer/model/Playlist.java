package com.example.musicplayer.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlist")
public class Playlist {
    private final int count;
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;

    public Playlist(String title, int count) {
        this.title = title;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }
}
