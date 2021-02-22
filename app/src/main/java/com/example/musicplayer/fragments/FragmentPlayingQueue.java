package com.example.musicplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.PlayingQueueAdapter;
import com.example.musicplayer.model.Library;
import com.example.musicplayer.model.Song;

import java.util.ArrayList;

public class FragmentPlayingQueue extends Fragment implements PlayingQueueAdapter.OnItemClickListener {
    private ArrayList<Song> mSongList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playing_queue, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rvPlayingQueue);
        mSongList = Library.getInstance().getAllSongsList();
        PlayingQueueAdapter adapter = new PlayingQueueAdapter(getContext(), mSongList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onItemClickListener(int position) {
//        musicService.setSong(Integer.parseInt(view.getTag().toString()));
//        musicService.playSong();
    }
}
