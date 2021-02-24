package com.example.musicplayer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.PlaylistSong;

@Database(entities = {
        Playlist.class,
        PlaylistSong.class},
        version = 1)
public abstract class PlaylistDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "db_playlist";
    private static PlaylistDatabase instance;

    public static synchronized PlaylistDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PlaylistDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract PlaylistDAO playlistDAO();

    public abstract PlaylistSongDAO playlistSongDAO();
}
