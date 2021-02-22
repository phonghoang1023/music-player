package com.example.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.musicplayer.base.IntentAction;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {
    public static final String TIMER_SERVICE_EVENT = "service.COUNTDOWN_TIMER";
    public static final String TIMER_FINISHED = "service.COUNTDOWN_FINISHED";
    private final Handler handler = new Handler();
    public int countDown;
    private Timer StopMusicTimer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        countDown = intent.getIntExtra(IntentAction.EXTRA_COUNTDOWN_TIMER, 0);
        Log.i("====TimerService", " started " + countDown);
        if (StopMusicTimer != null) {
            StopMusicTimer.cancel();
            Log.i("====TimerService", " cancel old service ");
        }
        StopMusicTimer = new Timer();
        StopMusicTimer.schedule(new StopMusicTask(), countDown * 1000);
        return START_STICKY;
    }

    private class StopMusicTask extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(TIMER_SERVICE_EVENT);
                    intent.putExtra(TIMER_FINISHED, true);
                    LocalBroadcastManager.getInstance(TimerService.this).sendBroadcast(intent);
                    stopService(new Intent(getApplicationContext(), MusicService.class));
                    Log.i("====TimerService", " request pausing music");
                    stopSelf();
                }
            });
        }
    }
}