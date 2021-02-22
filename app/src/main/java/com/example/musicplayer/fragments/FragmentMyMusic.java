package com.example.musicplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.FragmentMusicAdapter;
import com.example.musicplayer.model.Library;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FragmentMyMusic extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentMusicAdapter adapter = new FragmentMusicAdapter(this);
        ViewPager2 viewPager2 = view.findViewById(R.id.viewPager2);
        viewPager2.setAdapter(adapter);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("RECENT");
                    break;
                case 1:
                    tab.setText("PLAYLISTS");
                    break;
                case 2:
                    tab.setText("SONGS" + " (" +
                            Library.getInstance().getNumOfSongs() + ")");
                    break;
            }
        }).attach();
    }
}
