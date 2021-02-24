package com.example.musicplayer.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.PlaylistsAdapter;
import com.example.musicplayer.database.PlaylistDatabase;
import com.example.musicplayer.model.Playlist;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;


public class PlaylistBottomSheet extends BottomSheetDialogFragment {
    private final PlaylistsAdapter.PlaylistClickListener mListener;

    public PlaylistBottomSheet(PlaylistsAdapter.PlaylistClickListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet, null);
        bottomSheetDialog.setContentView(view);

        RecyclerView rvPlaylist = view.findViewById(R.id.rvPlaylist);
        rvPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<Playlist> listPlaylist = (ArrayList<Playlist>) PlaylistDatabase.getInstance(getContext())
                .playlistDAO().getListPlaylist();
        PlaylistsAdapter adapter = new PlaylistsAdapter(position -> mListener.onPlaylistSelected(position));
        adapter.setPlaylist(listPlaylist);
        rvPlaylist.setAdapter(adapter);
        return bottomSheetDialog;
    }
}
