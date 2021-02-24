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
import com.example.musicplayer.database.PlaylistDatabase;
import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.PlaylistSong;
import com.example.musicplayer.model.Song;
import com.example.musicplayer.model.SongsManager;

import java.util.ArrayList;

public class FragmentSongList extends Fragment {
    public static final String PLAYING_LIST = "fragments.FragmentSongList.PLAYING_LIST";
    public static final String POSITION = "fragments.FragmentSongList.POSITION";
    private ArrayList<Song> mSongList;
    private Song selectedSong;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rvPlaylist);
        mSongList = SongsManager.getInstance().getAllSongsList();
        SongsAdapter adapter = new SongsAdapter(mSongList, new SongsAdapter.SongsClickListener() {
            @Override
            public void onSongSelected(int position) {
                playSelectedSong(position);
            }

            @Override
            public void onIconMoreSettingsClicked(int position) {
                selectedSong = mSongList.get(position);
                showSettingBottomDialog();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void showSettingBottomDialog() {
        FragmentSettingsBottomSheet bottomSheetDialog = new FragmentSettingsBottomSheet(position -> {
            switch (position) {
                case 0: //R.string.add_to_queue:
                    break;
                case 1: //R.string.add_to_playlist:
                    showListOfPlaylist();
                    break;
                case 2: //R.string.add_to_favorite:
                    break;
                case 3: //R.string.delete_from_device:
                    break;
            }
        });
        bottomSheetDialog.show(getFragmentManager(), bottomSheetDialog.getTag());
    }

    private void showListOfPlaylist() {
        PlaylistBottomSheet playlistBottomSheet = new PlaylistBottomSheet(position -> addSongToSelectedPlaylist(position));
        playlistBottomSheet.show(getFragmentManager(), playlistBottomSheet.getTag());
    }

    private void addSongToSelectedPlaylist(int position) {
        ArrayList<Playlist> listPlaylist = (ArrayList<Playlist>) PlaylistDatabase.getInstance(getContext())
                .playlistDAO().getListPlaylist();
        Playlist selectedPlaylist = listPlaylist.get(position);

        PlaylistSong playlistSong = new PlaylistSong(selectedPlaylist.getId(), selectedSong.getId());
        PlaylistDatabase.getInstance(getContext()).playlistSongDAO().insertPlaylistSong(playlistSong);
        Toast.makeText(getContext(), "Added to playlist", Toast.LENGTH_SHORT).show();
    }


    private void playSelectedSong(int position) {
        if (mSongList != null && !mSongList.isEmpty()) {
            Intent intent = new Intent(getContext(), NowPlayingActivity.class);
            intent.putExtra(PLAYING_LIST, "mSongList");
            intent.putExtra(POSITION, position);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No media to play", Toast.LENGTH_SHORT).show();
        }
    }
}
