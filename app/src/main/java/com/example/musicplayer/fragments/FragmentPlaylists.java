package com.example.musicplayer.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.PlaylistsAdapter;
import com.example.musicplayer.database.PlaylistDatabase;
import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.PlaylistSong;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FragmentPlaylists extends Fragment implements View.OnClickListener {
    FloatingActionButton fabAddNewPlaylist;
    EditText edtPlaylistName;
    private ArrayList<Playlist> mPlaylist;
    private PlaylistsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlists, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rvPlaylist);
        fabAddNewPlaylist = view.findViewById(R.id.fabAddNewPlaylist);

        fabAddNewPlaylist.setOnClickListener(this);

        mAdapter = new PlaylistsAdapter(position -> Toast.makeText(getContext(), " " + position, Toast.LENGTH_SHORT).show());
        updatePlaylistView();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddNewPlaylist:
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_add_new_playlist, null);

                edtPlaylistName = view.findViewById(R.id.edtPlaylistName);
                Button btnCreatePlaylist = view.findViewById(R.id.btnCreatePlaylist);
                Button btnCancel = view.findViewById(R.id.btnCancel);

                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(view).create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                AlertDialog finalAlertDialog = alertDialog;
                btnCreatePlaylist.setOnClickListener(v1 -> {
                    addNewPlaylist();
                    updatePlaylistView();
                    finalAlertDialog.dismiss();
                });
                btnCancel.setOnClickListener(v12 -> finalAlertDialog.dismiss());
                break;
        }
    }

    private void updatePlaylistView() {
        mPlaylist = (ArrayList<Playlist>) PlaylistDatabase.getInstance(getContext()).playlistDAO().getListPlaylist();
        mAdapter.setPlaylist(mPlaylist);
    }

    private void addNewPlaylist() {
        String playlistTitle = edtPlaylistName.getText().toString().trim();
        if (!TextUtils.isEmpty(playlistTitle)) {
            Playlist playlist = new Playlist(playlistTitle, 14);
            PlaylistDatabase.getInstance(getContext()).playlistDAO().insertPlaylist(playlist);
            Toast.makeText(getContext(), "Created playlist", Toast.LENGTH_SHORT).show();
        }
    }

    private void addSongsToPlaylist(int playlistId, ArrayList<Integer> listSongId) {
        if (listSongId == null) {
            return;
        }
        for (int songId : listSongId) {
            PlaylistSong playlistSong = new PlaylistSong(playlistId, songId);
            PlaylistDatabase.getInstance(getContext()).playlistSongDAO().insertPlaylistSong(playlistSong);
        }
        Toast.makeText(getContext(), "Added to playlist", Toast.LENGTH_SHORT).show();
    }
}
