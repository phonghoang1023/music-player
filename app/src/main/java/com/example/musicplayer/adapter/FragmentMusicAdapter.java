package com.example.musicplayer.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.musicplayer.fragments.FragmentPlaylists;
import com.example.musicplayer.fragments.FragmentRecent;
import com.example.musicplayer.fragments.FragmentSongList;

public class FragmentMusicAdapter extends FragmentStateAdapter {

    public FragmentMusicAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentRecent();
            case 1:
                return new FragmentPlaylists();
            case 2:
                return new FragmentSongList();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
