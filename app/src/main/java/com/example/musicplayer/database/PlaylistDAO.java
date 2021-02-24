package com.example.musicplayer.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.musicplayer.model.Playlist;

import java.util.List;

@Dao
public interface PlaylistDAO {
    @Insert
    void insertPlaylist(Playlist playlist);

    @Query("SELECT * FROM playlist")
    List<Playlist> getListPlaylist();
}