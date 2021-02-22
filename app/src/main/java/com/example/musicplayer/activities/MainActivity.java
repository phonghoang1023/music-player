package com.example.musicplayer.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicplayer.R;
import com.example.musicplayer.fragments.FragmentMyMusic;
import com.example.musicplayer.fragments.FragmentSettings;
import com.example.musicplayer.fragments.FragmentYoutube;
import com.example.musicplayer.model.Library;
import com.example.musicplayer.model.Song;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG_MY_MUSIC = "FragmentMyMusic";
    private static final String TAG_YOUTUBE = "FragmentYoutube";
    private static final String TAG_SETTINGS = "FragmentSettings";
    private BottomNavigationView mBottomNavigation;
    private long mPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        loadFragment(new FragmentMyMusic(), TAG_MY_MUSIC);
        scanStorageForALlSongs();
        mBottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent i = new Intent(MainActivity.this, SplashScreen.class);
            startActivity(i);
        }
    }

    private void scanStorageForALlSongs() {
        ArrayList<Song> list = new ArrayList<>();
        Uri uriSong = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri uriAlbum = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor cursorSong = getContentResolver()
                .query(uriSong, null, null, null, null);
        Cursor cursorAlbum = getContentResolver()
                .query(uriAlbum, null, null, null, null);
        if (cursorSong == null || cursorAlbum == null) {
            Toast.makeText(this, "Failed to get songs", Toast.LENGTH_SHORT).show();
        } else if (!cursorSong.moveToFirst() || !cursorAlbum.moveToFirst()) {
            Toast.makeText(this, "No song found on device", Toast.LENGTH_SHORT).show();
        } else {
            int id = cursorSong.getColumnIndex(MediaStore.Audio.Media._ID);
            int title = cursorSong.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artist = cursorSong.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int duration = cursorSong.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int album = cursorSong.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int path = cursorSong.getColumnIndex(MediaStore.MediaColumns.DATA);
            int albumImageId = cursorAlbum.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            Log.i(">>>>ID", albumImageId + "");
            do {
                Drawable albumImage = Drawable.createFromPath(cursorAlbum.getString(albumImageId));
                list.add(new Song(cursorSong.getInt(id),
                        cursorSong.getString(title),
                        cursorSong.getString(artist),
                        cursorSong.getString(duration),
                        cursorSong.getString(album),
                        cursorSong.getString(path),
                        albumImage));
                Log.i(">>>>DRAWABLE", albumImage + "");
            } while (cursorSong.moveToNext());
        }
        if (cursorAlbum != null) {
            cursorAlbum.close();
        }
        if (cursorSong != null) {
            cursorSong.close();
        }
        Library.getInstance().setAllSongsList(list);
    }

    private void loadFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameContainer, fragment, tag)
                .commit();
    }

    private void initView() {
        mBottomNavigation = findViewById(R.id.bottomNavigation);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (manager.findFragmentByTag(TAG_SETTINGS) != null) {
            transaction.remove(manager.findFragmentByTag(TAG_SETTINGS));
        }
        switch (item.getItemId()) {
            case R.id.action_my_music:
                if (manager.findFragmentByTag(TAG_MY_MUSIC) != null) {
                    transaction.show(manager.findFragmentByTag(TAG_MY_MUSIC));
                } else {
                    transaction.add(R.id.frameContainer, new FragmentMyMusic(), TAG_MY_MUSIC);
                }
                if (manager.findFragmentByTag(TAG_YOUTUBE) != null) {
                    transaction.hide(manager.findFragmentByTag(TAG_YOUTUBE));
                }
                transaction.commit();
                return true;

            case R.id.action_youtube:
                if (manager.findFragmentByTag(TAG_YOUTUBE) != null) {
                    transaction.show(manager.findFragmentByTag(TAG_YOUTUBE));
                } else {
                    transaction.add(R.id.frameContainer, new FragmentYoutube(), TAG_YOUTUBE);
                }
                if (manager.findFragmentByTag(TAG_MY_MUSIC) != null) {
                    transaction.hide(manager.findFragmentByTag(TAG_MY_MUSIC));
                }
                transaction.commit();
                return true;
            case R.id.action_settings:
                transaction.add(R.id.frameContainer, new FragmentSettings(), TAG_SETTINGS);
                if (manager.findFragmentByTag(TAG_MY_MUSIC) != null) {
                    transaction.hide(manager.findFragmentByTag(TAG_MY_MUSIC));
                }
                if (manager.findFragmentByTag(TAG_YOUTUBE) != null) {
                    transaction.hide(manager.findFragmentByTag(TAG_YOUTUBE));
                }
                transaction.commit();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            System.exit(0);
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        mPressedTime = System.currentTimeMillis();
    }
}