package com.example.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.musicplayer.base.IntentAction;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {
    private Timer StopMusicTimer;
    private final Handler handler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int countDown = intent.getIntExtra(IntentAction.EXTRA_COUNTDOWN_TIMER, 0);
        if (StopMusicTimer != null) {
            StopMusicTimer.cancel();
        }
        StopMusicTimer = new Timer();
        StopMusicTimer.schedule(new StopMusicTask(), countDown * 1000);
        return START_NOT_STICKY;
    }

    private class StopMusicTask extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //update timer icon
                    Intent activityIntent = new Intent(IntentAction.ACTION_TIMER_FINISHED);
                    LocalBroadcastManager.getInstance(TimerService.this).sendBroadcast(activityIntent);
                    //pause music service
                    Intent serviceIntent = new Intent(TimerService.this, MusicService.class);
                    serviceIntent.setAction(IntentAction.ACTION_PAUSE_PLAYER);
                    startService(serviceIntent);

                    stopSelf();
                }
            });
        }
    }
}