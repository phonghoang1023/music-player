package com.example.musicplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.SongsAdapter;
import com.example.musicplayer.base.IntentAction;
import com.example.musicplayer.database.PlaylistDatabase;
import com.example.musicplayer.model.Song;
import com.example.musicplayer.model.SongsManager;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private RecyclerView mRvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRvList = findViewById(R.id.rvList);
        Toolbar toolbar = findViewById(R.id.toolbarListActivity);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        int playlistId = getIntent().getIntExtra(IntentAction.EXTRA_PLAYLIST_ID, -1);
        if (playlistId != -1) {
            ArrayList<Song> songList = getSongsByPlaylistId(playlistId);
            setUpRecyclerView(songList, playlistId);
        }
    }

    private void setUpRecyclerView(ArrayList<Song> songList, int playlistId) {
        if (songList == null || songList.isEmpty()) {
            return;
        }
        SongsAdapter adapter = new SongsAdapter(songList, new SongsAdapter.SongsClickListener() {
            @Override
            public void onSongSelected(int position) {
                playSelectedSong(playlistId, position);
            }

            @Override
            public void onIconMoreSettingsClicked(int position) {

            }
        });
        mRvList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRvList.setAdapter(adapter);
    }

    private void playSelectedSong(int playlistId, int position) {
        Intent intent = new Intent(this, NowPlayingActivity.class);
        intent.putExtra(IntentAction.EXTRA_PLAYING_LIST_ID, playlistId);
        intent.putExtra(IntentAction.EXTRA_POSITION, position);
        startActivity(intent);
    }

    private ArrayList<Song> getSongsByPlaylistId(int playlistId) {
        ArrayList<Integer> songIdList = (ArrayList<Integer>) PlaylistDatabase.getInstance(this)
                .playlistSongDAO().getSongIdFromPlaylistId(playlistId);
        ArrayList<Song> songList = new ArrayList<>();
        for (Song song : SongsManager.getInstance().getAllSongsList()) {
            for (int songId : songIdList) {
                if (song.getId() == songId) {
                    songList.add(song);
                }
            }
        }
        return songList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.action_settings:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}