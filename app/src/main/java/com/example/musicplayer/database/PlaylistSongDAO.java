package com.example.musicplayer.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.musicplayer.model.PlaylistSong;

import java.util.List;

@Dao
public interface PlaylistSongDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPlaylistSong(PlaylistSong playlistSong);

    @Query("SELECT songId FROM playlist_song WHERE playlistId LIKE :playlistId")
    List<Integer> getSongFromPlaylistId(int playlistId);
}
