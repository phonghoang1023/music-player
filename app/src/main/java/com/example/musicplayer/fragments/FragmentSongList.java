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
import com.example.musicplayer.base.IntentAction;
import com.example.musicplayer.database.PlaylistDatabase;
import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.PlaylistSong;
import com.example.musicplayer.model.Song;
import com.example.musicplayer.model.SongsManager;

import java.util.ArrayList;

public class FragmentSongList extends Fragment {
    private ArrayList<Song> mSongList;
    private Song selectedSong;
    private RecyclerView mRecyclerView;
    private FragmentSettingsBottomSheet bottomSheetDialog;
    private PlaylistBottomSheet playlistBottomSheet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        mRecyclerView = view.findViewById(R.id.rvBottomSheet);
        mSongList = SongsManager.getInstance().getAllSongsList();
        setUpRecyclerView();

        return view;
    }

    private void setUpRecyclerView() {
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);
    }

    private void showSettingBottomDialog() {
        String[] settingsList = {
//                getString(R.string.add_to_queue),
                getString(R.string.add_to_playlist),
//                getString(R.string.add_to_favorite),
                getString(R.string.delete_from_device)};

        bottomSheetDialog = new FragmentSettingsBottomSheet(settingsList, position -> {
            switch (position) {
                case 0: //R.string.add_to_queue:
                    break;
                case 1: //R.string.add_to_playlist:
                    showPlaylistInBottomDialog();
                    break;
                case 2: //R.string.add_to_favorite:
                    break;
                case 3: //R.string.delete_from_device:
                    break;
            }
        });
        bottomSheetDialog.show(getFragmentManager(), bottomSheetDialog.getTag());
    }

    private void showPlaylistInBottomDialog() {
        playlistBottomSheet = new PlaylistBottomSheet(this::addSongToSelectedPlaylist);
        playlistBottomSheet.show(getFragmentManager(), playlistBottomSheet.getTag());
        bottomSheetDialog.dismiss();
    }

    private void addSongToSelectedPlaylist(int playlistId) {
        ArrayList<Playlist> listPlaylist = (ArrayList<Playlist>) PlaylistDatabase.getInstance(getContext())
                .playlistDAO().getListPlaylist();
        Playlist selectedPlaylist = null;
        for (Playlist list : listPlaylist) {
            if (list.getId() == playlistId) {
                selectedPlaylist = list;
            }
        }
        PlaylistSong playlistSong = new PlaylistSong(selectedPlaylist.getId(), selectedSong.getId());
        PlaylistDatabase.getInstance(getContext()).playlistSongDAO().insertPlaylistSong(playlistSong);
        Toast.makeText(getContext(), "Added to playlist", Toast.LENGTH_SHORT).show();
        playlistBottomSheet.dismiss();
    }

    private void playSelectedSong(int position) {
        if (mSongList != null && !mSongList.isEmpty()) {
            Intent intent = new Intent(getContext(), NowPlayingActivity.class);
            intent.putExtra(IntentAction.EXTRA_PLAYING_LIST_ID, IntentAction.ALL_SONGS);
            intent.putExtra(IntentAction.EXTRA_POSITION, position);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "No media to play", Toast.LENGTH_SHORT).show();
        }
    }
}
