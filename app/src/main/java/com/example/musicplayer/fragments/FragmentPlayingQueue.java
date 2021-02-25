package com.example.musicplayer.fragments;

import android.content.Context;
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
import com.example.musicplayer.activities.NowPlayingActivity;
import com.example.musicplayer.adapter.PlayingQueueAdapter;
import com.example.musicplayer.model.Song;

import java.util.ArrayList;

public class FragmentPlayingQueue extends Fragment implements PlayingQueueAdapter.PlayingQueueClickListener {
    private ArrayList<Song> mSongList;
    private PlayingQueueChangesListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (PlayingQueueChangesListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playing_queue, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rvPlayingQueue);

        NowPlayingActivity nowPlayingActivity = (NowPlayingActivity) getActivity();
        mSongList = nowPlayingActivity.getSongList();
        PlayingQueueAdapter adapter = new PlayingQueueAdapter(mSongList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onQueuedSongSelected(int position) {
        mListener.onPlayingQueueChanges(position);
    }

    public interface PlayingQueueChangesListener {
        void onPlayingQueueChanges(int pos);
    }
}
