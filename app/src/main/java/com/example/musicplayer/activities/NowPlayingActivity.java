package com.example.musicplayer.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.NowPlayingAdapter;
import com.example.musicplayer.base.IntentAction;
import com.example.musicplayer.database.PlaylistDatabase;
import com.example.musicplayer.fragments.FragmentPlayingQueue;
import com.example.musicplayer.fragments.FragmentTimerDialog;
import com.example.musicplayer.model.Song;
import com.example.musicplayer.model.SongsManager;
import com.example.musicplayer.service.MusicService;
import com.example.musicplayer.service.TimerService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NowPlayingActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener,
        FragmentTimerDialog.TimerListener, FragmentPlayingQueue.PlayingQueueChangesListener {
    public static final String TAG_TIMER_DIALOG = "TAG_TIMER_DIALOG";
    private ViewPager2 viewPager2;
    private TabLayout tabDots;
    private Toolbar toolbar;
    private ImageView imgPlay, imgNext, imgPrev, imgRepeat, imgShuffle, imgTimer, imgAddToPlaylist;
    private TextView txtCurrentProgress, txtDuration, txtPlayingTitle, txtPlayingArtist;
    private boolean mMusicBound;
    private Intent mIntent;
    private MusicService mMusicService;
    private int mPosition;
    private ArrayList<Song> mSongList;
    private SeekBar mSeekBar;
    private Handler mHandler;
    private Runnable mRunnable;
    private BroadcastReceiver mReceiver;
    private boolean isNewActivity;
    private final ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mMusicService = binder.getService();
            mMusicBound = true;
            if (mSongList != null && !mSongList.isEmpty() && isNewActivity) {
                mMusicService.setList(mSongList);
                mMusicService.setSongPosition(mPosition);
                mMusicService.playSong();
                isNewActivity = false;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicBound = false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        initView();
        setUpViewPager2();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startMusicService();
        initPlaylist();
        setupClickListener();
        onLocalBroadcastReceived();
        isNewActivity = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(mIntent, musicConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMusicService != null) {
            updatePlayerInfo();
            updatePlayerProgress();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMusicBound) {
            unbindService(musicConnection);
            mMusicBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void initView() {
        viewPager2 = findViewById(R.id.viewPager2_now_playing);
        tabDots = findViewById(R.id.tabDots);
        toolbar = findViewById(R.id.toolbarNowPlaying);
        txtCurrentProgress = findViewById(R.id.txtCurrentTime);
        txtDuration = findViewById(R.id.txtDuration);
        txtPlayingTitle = findViewById(R.id.txtPlayingTitle);
        txtPlayingArtist = findViewById(R.id.txtPlayingArtist);
        imgPlay = findViewById(R.id.imgPlay);
        imgNext = findViewById(R.id.imgNext);
        imgPrev = findViewById(R.id.imgPrev);
        imgRepeat = findViewById(R.id.imgRepeat);
        imgShuffle = findViewById(R.id.imgShuffle);
        imgTimer = findViewById(R.id.imgTimer);
        imgAddToPlaylist = findViewById(R.id.imgAddToPlaylist);
        mSeekBar = findViewById(R.id.sbPlaying);
    }

    private void setUpViewPager2() {
        NowPlayingAdapter adapter = new NowPlayingAdapter(this);
        viewPager2.setAdapter(adapter);
        viewPager2.setCurrentItem(1);
        new TabLayoutMediator(tabDots, viewPager2, (tab, position) -> {
        }).attach();
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 1) {
                    imgTimer.setVisibility(View.VISIBLE);
                    imgAddToPlaylist.setVisibility(View.VISIBLE);
                } else {
                    imgTimer.setVisibility(View.GONE);
                    imgAddToPlaylist.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setupClickListener() {
        imgPlay.setOnClickListener(this);
        imgPrev.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        imgRepeat.setOnClickListener(this);
        imgShuffle.setOnClickListener(this);
        imgTimer.setOnClickListener(this);
        imgAddToPlaylist.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    private void initPlaylist() {
        int playlistId = getIntent().getIntExtra(IntentAction.EXTRA_PLAYING_LIST_ID, -2);
        mSongList = getSongListById(playlistId);
        mPosition = getIntent().getIntExtra(IntentAction.EXTRA_POSITION, 0);
    }

    private void startMusicService() {
        mIntent = new Intent(this, MusicService.class);
        mIntent.setAction(IntentAction.ACTION_START_MUSIC_FOREGROUND);
        startService(mIntent);
    }

    private ArrayList<Song> getSongListById(int playlistId) {
        ArrayList<Song> songList = new ArrayList<>();
        if (playlistId == IntentAction.ALL_SONGS) {
            songList = SongsManager.getInstance().getAllSongsList();
        } else {
            ArrayList<Integer> songIdList = (ArrayList<Integer>) PlaylistDatabase.getInstance(this)
                    .playlistSongDAO().getSongIdFromPlaylistId(playlistId);
            for (Song song : SongsManager.getInstance().getAllSongsList()) {
                for (int songId : songIdList) {
                    if (song.getId() == songId) {
                        songList.add(song);
                    }
                }
            }
        }
        return songList;
    }

    private void updatePlayerInfo() {
        if (mMusicService == null) return;
        Song playingSong = mMusicService.getPlayingSong();
        txtDuration.setText(new SimpleDateFormat("mm:ss")
                .format(mMusicService.getDuration()));
        txtPlayingTitle.setText(playingSong.getTitle());
        txtPlayingArtist.setText(playingSong.getArtist());
        mSeekBar.setMax(Integer.parseInt(playingSong.getDuration()));
    }

    private void updatePlayerProgress() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mMusicService.isPlaying()) {
                    imgPlay.setImageResource(R.drawable.ic_pause);
                } else {
                    imgPlay.setImageResource(R.drawable.ic_play);
                }
                txtCurrentProgress.setText(new SimpleDateFormat("mm:ss")
                        .format(mMusicService.getProgress()));
                mSeekBar.setProgress(mMusicService.getProgress());
                mHandler.postDelayed(this, 500);
            }
        };
        if (mMusicService != null) mHandler.post(mRunnable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgPlay:
                if (mMusicService.isPlaying()) {
                    mMusicService.pause();
                } else {
                    mMusicService.start();
                }
                updatePlayerInfo();
                break;
            case R.id.imgNext:
                mMusicService.playNext();
                break;
            case R.id.imgPrev:
                mMusicService.playPrev();
                break;
            case R.id.imgRepeat:
                if (mMusicService.getRepeatMode() == MusicService.REPEAT_OFF) {
                    mMusicService.setRepeatMode(MusicService.REPEAT_ON);
                    imgRepeat.setImageResource(R.drawable.repeat_on);
                } else if (mMusicService.getRepeatMode() == MusicService.REPEAT_ON) {
                    mMusicService.setRepeatMode(MusicService.REPEAT_ONCE);
                    imgRepeat.setImageResource(R.drawable.repeat_once);
                } else {
                    mMusicService.setRepeatMode(MusicService.REPEAT_OFF);
                    imgRepeat.setImageResource(R.drawable.repeat_off);
                }
                break;
            case R.id.imgShuffle:
                if (mMusicService.isShuffle()) {
                    mMusicService.setShuffle(false);
                    imgShuffle.setImageResource(R.drawable.shuffle_off);
                } else {
                    mMusicService.setShuffle(true);
                    imgShuffle.setImageResource(R.drawable.shuffle_on);
                }
                break;
            case R.id.imgTimer:
                FragmentTimerDialog timerDialog = new FragmentTimerDialog();
                FragmentManager manager = getSupportFragmentManager();
                timerDialog.show(manager, TAG_TIMER_DIALOG);
                break;
            case R.id.imgAddToPlaylist:
                break;
        }
    }

    public ArrayList<Song> getSongList() {
        return mSongList;
    }

    @Override
    public void setOnTimerConfirmed(int countDown) {
        Intent intent = new Intent(this, TimerService.class);
        if (countDown != 0) {
            imgTimer.setImageResource(R.drawable.timer_confirmed);
            Toast.makeText(this, "Music will stop after " + countDown + " minutes", Toast.LENGTH_SHORT).show();
            intent.putExtra(IntentAction.EXTRA_COUNTDOWN_TIMER, countDown);
            startService(intent);
        } else {
            imgTimer.setImageResource(R.drawable.timer_default);
            stopService(intent);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        txtCurrentProgress.setText(new SimpleDateFormat("mm:ss").format(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mMusicService.seekTo(mSeekBar.getProgress());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_now_playing_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onLocalBroadcastReceived() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent == null) return;
                String action = intent.getAction();
                switch (action) {
                    case IntentAction.ACTION_START_PLAYING:
                        updatePlayerInfo();
                    case IntentAction.ACTION_MEDIA_COMPLETE:
                        updatePlayerProgress();
                        break;
                    case IntentAction.ACTION_STOP_MUSIC_FOREGROUND:
                        unbindService(musicConnection);
                        mMusicBound = false;
                        finish();
                        break;
                    case IntentAction.ACTION_TIMER_FINISHED:
                        imgTimer.setImageResource(R.drawable.timer_default);
                        break;
                }
            }
        };
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(mReceiver, new IntentFilter(IntentAction.ACTION_STOP_MUSIC_FOREGROUND));
        broadcastManager.registerReceiver(mReceiver, new IntentFilter(IntentAction.ACTION_START_PLAYING));
        broadcastManager.registerReceiver(mReceiver, new IntentFilter(IntentAction.ACTION_MEDIA_COMPLETE));
        broadcastManager.registerReceiver(mReceiver, new IntentFilter(IntentAction.ACTION_TIMER_FINISHED));
    }

    @Override
    public void onPlayingQueueChanges(int pos) {
        if (mMusicService != null) {
            mMusicService.setSongPosition(pos);
            mMusicService.playSong();
        }
    }
}