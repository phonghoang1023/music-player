package com.example.musicplayer.model;

import android.graphics.drawable.Drawable;

public class Song {
    private int mId;
    private String mTitle;
    private String mArtist;
    private String mDuration;
    private String mAlbum;
    private String mPath;
    private Drawable mAlbumCover;

    public Song() {
    }

    public Song(int id, String mTitle, String mArtist, String mDuration,
                String mAlbum, String mPath, Drawable mAlbumCover) {
        this.mId = id;
        this.mTitle = mTitle;
        this.mArtist = mArtist;
        this.mDuration = mDuration;
        this.mAlbum = mAlbum;
        this.mPath = mPath;
        this.mAlbumCover = mAlbumCover;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getArtist() {
        return mArtist;
    }

    public String getDuration() {
        return mDuration;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public String getPath() {
        return mPath;
    }

    public Drawable getAlbumCover() {
        return mAlbumCover;
    }
}
