package com.example.musicplayer.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.musicplayer.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView navigationView;
    private long mPressedTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        navigationView = findViewById(R.id.bottomNavigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent i = new Intent(this, SplashScreen.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
//            finish();
            System.exit(0);
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        mPressedTime = System.currentTimeMillis();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_my_music:
                startActivity(new Intent(BaseActivity.this, MainActivity.class));
                break;
            case R.id.action_youtube:
                startActivity(new Intent(BaseActivity.this, NowPlayingActivity.class));
                break;
            case R.id.action_settings:
                startActivity(new Intent(BaseActivity.this, SettingsActivity.class));
                break;
        }
        return true;
    }

    void updateNavigationBarState() {
        int itemId = getBottomNavigationMenuItemId();
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    abstract int getContentViewId();

    abstract int getBottomNavigationMenuItemId();
}

