package com.example.musicplayer.model;

import android.graphics.drawable.Drawable;


public class Song {
    private int id;
    private String title;
    private String artist;
    private String duration;
    private String album;
    private String path;
    private Drawable albumCover;

    public Song() {
    }

    public Song(int id, String title, String artist, String duration,
                String album, String path, Drawable albumCover) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.album = album;
        this.path = path;
        this.albumCover = albumCover;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getDuration() {
        return duration;
    }

    public String getAlbum() {
        return album;
    }

    public String getPath() {
        return path;
    }

    public Drawable getAlbumCover() {
        return albumCover;
    }
}
