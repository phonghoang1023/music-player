package com.example.musicplayer.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.musicplayer.fragments.FragmentLyric;
import com.example.musicplayer.fragments.FragmentNowPlaying;
import com.example.musicplayer.fragments.FragmentPlayingQueue;

public class NowPlayingAdapter extends FragmentStateAdapter {


    public NowPlayingAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentPlayingQueue();
            case 1:
                return new FragmentNowPlaying();
            case 2:
                return new FragmentLyric();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
