package com.example.musicplayer.model;

import java.util.ArrayList;

public class SongsManager {
    private static ArrayList<Song> list;

    private static SongsManager instance;

    public static SongsManager getInstance() {
        if (instance == null) {
            instance = new SongsManager();
        }
        return instance;
    }

    public ArrayList<Song> getAllSongsList() {
        return list;
    }

    public void setAllSongsList(ArrayList<Song> list) {
        SongsManager.list = list;
    }

    public int getNumOfSongs() {
        if (list != null && !list.isEmpty()) {
            return list.size();
        }
        return 0;
    }
}
