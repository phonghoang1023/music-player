package com.example.musicplayer.model;

import java.util.ArrayList;

public class Library {
    private static ArrayList<Song> list;

    private static Library instance;

    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    public ArrayList<Song> getAllSongsList() {
        return list;
    }

    public void setAllSongsList(ArrayList<Song> list) {
        Library.list = list;
    }

    public int getNumOfSongs() {
        if (list != null && !list.isEmpty()) {
            return list.size();
        }
        return 0;
    }
}
