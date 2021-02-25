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
import com.example.musicplayer.database.PlaylistDatabase;
import com.example.musicplayer.model.SongsManager;
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
                    int playlistCount = PlaylistDatabase.getInstance(getContext())
                            .playlistDAO().getListPlaylist().size();
                    tab.setText("PLAYLISTS " + "(" + playlistCount + ")");
                    break;
                case 1:
                    int songCount = SongsManager.getInstance().getNumOfSongs();
                    tab.setText("SONGS " + "(" + songCount + ")");
                    break;
            }
        }).attach();
    }
}
