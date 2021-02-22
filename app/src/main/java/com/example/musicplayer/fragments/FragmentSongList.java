package com.example.musicplayer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.activities.NowPlayingActivity;
import com.example.musicplayer.adapter.SongsAdapter;
import com.example.musicplayer.model.Library;
import com.example.musicplayer.model.Song;

import java.util.ArrayList;

public class FragmentSongList extends Fragment implements SongsAdapter.OnItemClickListener {
    public static final String PLAYING_LIST = "fragments.FragmentSongList.PLAYING_LIST";
    public static final String POSITION = "fragments.FragmentSongList.POSITION";
    public static final String START_PLAYING = "fragments.FragmentSongList.START_PLAYING";
    private ArrayList<Song> mSongList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rvSongList);
        mSongList = Library.getInstance().getAllSongsList();
        SongsAdapter adapter = new SongsAdapter(getContext(), mSongList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onItemClickListener(int position) {
        if (mSongList != null && !mSongList.isEmpty()) {
            Intent intent = new Intent(getContext(), NowPlayingActivity.class);
            intent.putExtra(PLAYING_LIST, "mSongList");
            intent.putExtra(POSITION, position);
            intent.putExtra(START_PLAYING, true);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "No media to play", Toast.LENGTH_SHORT).show();
        }
    }
}
