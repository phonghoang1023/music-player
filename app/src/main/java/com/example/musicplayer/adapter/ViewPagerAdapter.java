package com.example.musicplayer.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.musicplayer.fragments.FragmentMyMusic;
import com.example.musicplayer.fragments.FragmentSettings;
import com.example.musicplayer.fragments.FragmentYoutube;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentMyMusic();
            case 1:
                return new FragmentYoutube();
            case 2:
                return new FragmentSettings();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
