package com.example.musicplayer.model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlist_song", indices = @Index(value = {"playlistId", "songId"}, unique = true))
public class PlaylistSong {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final int playlistId;
    private final int songId;

    public PlaylistSong(int playlistId, int songId) {
        this.playlistId = playlistId;
        this.songId = songId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public int getSongId() {
        return songId;
    }
}
