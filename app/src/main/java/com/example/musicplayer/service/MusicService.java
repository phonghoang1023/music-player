package com.example.musicplayer.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.IdRes;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.musicplayer.R;
import com.example.musicplayer.base.IntentAction;
import com.example.musicplayer.model.Song;

import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {
    public static final int REPEAT_OFF = 0;
    public static final int REPEAT_ON = 1;
    public static final int REPEAT_ONCE = 2;
    public static final int NOTIFICATION_ID = 115;
    public final static String NOTIFICATION_CHANNEL_ID =
            "CHANNEL Music Player";
    private final IBinder musicBind = new MusicBinder();
    private boolean isShuffle;
    private int repeatMode = 0;
    private MediaPlayer player;
    private ArrayList<Song> songList;
    private int songPosition;
    private AudioManager audioManager;
    private NotificationReceiver mReceiver;
    private NotificationManager mNotificationManager;
    private Context mContext;
    private RemoteViews notificationView;
    private Notification notification;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        mContext = getApplicationContext();
        if (player == null) {
            player = new MediaPlayer();
            initMusicPlayer();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!requestAudioFocus()) {
            pause();
        }
        createNotificationChannel();
        startForeGroundService();
        if (intent != null) {
            String action = intent.getAction();
            switch (action) {
                case IntentAction.ACTION_START_MUSIC_FOREGROUND:
                    startForeGroundService();
                    registerNotificationReceiver();
                    break;
                case IntentAction.ACTION_PAUSE_PLAYER:
                    pause();
                    break;
                case IntentAction.ACTION_PLAY_PAUSE:
                    if (isPlaying()) {
                        pause();
                    } else {
                        start();
                    }
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (player != null) {
            player.release();
        }
        unregisterReceiver(mReceiver);
        removeAudioFocus();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (!requestAudioFocus()) {
            stopSelf();
        }
        return musicBind;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_music_player);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    name, importance);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mNotificationManager = getSystemService(NotificationManager.class);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    private void startForeGroundService() {
        notificationView = new RemoteViews(getPackageName(), R.layout.notification_layout);
        Intent intent = new Intent(IntentAction.ACTION_NOTIFICATION_CLICK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        notificationView.setOnClickPendingIntent(R.id.rlNotification, pendingIntent);
        notificationView.setOnClickPendingIntent(R.id.imgPrev, onButtonNotificationClick(R.id.imgPrev));
        notificationView.setOnClickPendingIntent(R.id.imgPlay, onButtonNotificationClick(R.id.imgPlay));
        notificationView.setOnClickPendingIntent(R.id.imgNext, onButtonNotificationClick(R.id.imgNext));
        notificationView.setOnClickPendingIntent(R.id.imgClose, onButtonNotificationClick(R.id.imgClose));
        if (songList != null) {
            notificationView.setTextViewText(R.id.songTitle, getPlayingSong().getTitle());
            notificationView.setTextViewText(R.id.songArtist, getPlayingSong().getArtist());
        }
        notificationView.setImageViewResource(R.id.imgPlay, isPlaying() ?
                R.drawable.round_pause_black_24dp : R.drawable.round_play_arrow_black_24dp);

        notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setVibrate(null)
                .setCustomBigContentView(notificationView)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    private void stopForeGroundService() {
        stopForeground(true);
        stopSelf();

    }

    private void updateNotification() {
        if (songList != null) {
            notificationView.setTextViewText(R.id.songTitle, getPlayingSong().getTitle());
            notificationView.setTextViewText(R.id.songArtist, getPlayingSong().getArtist());
        }
        notificationView.setImageViewResource(R.id.imgPlay, isPlaying() ?
                R.drawable.round_pause_black_24dp : R.drawable.round_play_arrow_black_24dp);
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    private PendingIntent onButtonNotificationClick(@IdRes int id) {
        Intent intent = new Intent(IntentAction.ACTION_NOTIFICATION_CLICK);
        intent.putExtra(IntentAction.EXTRA_IMG_CLICKED, id);
        return PendingIntent.getBroadcast(this, id, intent, 0);
    }

    private void registerNotificationReceiver() {
        IntentFilter filter = new IntentFilter(IntentAction.ACTION_NOTIFICATION_CLICK);
        mReceiver = new NotificationReceiver();
        registerReceiver(mReceiver, filter);
    }

    public void initMusicPlayer() {
        player.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> list) {
        songList = list;
    }

    public Song getPlayingSong() {
        return songList.get(songPosition);
    }

    public void playSong() {
        player.reset();
        Song playSong = getPlayingSong();
        String songPath = playSong.getPath();
        try {
            player.setDataSource(songPath);
        } catch (IOException e) {
            player.reset();
            Log.e("MusicService: ", "Error setting data source");
        }
        player.prepareAsync();
    }

    public void setSongPosition(int songIndex) {
        songPosition = songIndex;
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public void pause() {
        player.pause();
        updateNotification();
    }

    public void seekTo(int posn) {
        player.seekTo(posn);
    }

    public void start() {
        player.start();
        sendLocalBroadcast(IntentAction.ACTION_START_PLAYING);
        updateNotification();
    }

    public int getProgress() {
        return player.getCurrentPosition();
    }

    public int getDuration() {
        return Integer.parseInt(getPlayingSong().getDuration());
    }

    public void playNext() {
        songPosition++;
        if (songPosition == songList.size()) {
            songPosition = 0;
        }
        Log.i("=======", songList.size() + " - " + songPosition);
        playSong();
    }

    public void playPrev() {
        songPosition--;
        if (songPosition == 0) {
            songPosition = songList.size() - 1;
        }
        playSong();
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean isShuffle) {
        this.isShuffle = isShuffle;
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                if (!isPlaying()) {
                    start();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if (isPlaying()) pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (isPlaying()) player.setVolume(0.1f, 0.1f);
                break;
        }
    }

    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.abandonAudioFocus(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if ((repeatMode == REPEAT_OFF && songPosition != songList.size() - 1)
                || repeatMode == REPEAT_ON) {
            mp.reset();
            playNext();
            sendLocalBroadcast(IntentAction.ACTION_MEDIA_COMPLETE);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        start();
    }

    private void sendLocalBroadcast(String action) {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent(action);
        broadcastManager.sendBroadcast(intent);
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int id = intent.getIntExtra(IntentAction.EXTRA_IMG_CLICKED, -1);
            switch (id) {
                case R.id.rlNotification:
                    break;
                case R.id.imgPrev:
                    playPrev();
                    break;
                case R.id.imgPlay:
                    if (isPlaying()) {
                        pause();
                    } else {
                        start();
                    }
                    updateNotification();
                    break;
                case R.id.imgNext:
                    playNext();
                    break;
                case R.id.imgClose:
                    stopForeGroundService();
                    sendLocalBroadcast(IntentAction.ACTION_STOP_MUSIC_FOREGROUND);
                    break;
            }
        }
    }
}
