package com.example.musicplayer.activities;

import com.example.musicplayer.R;

public class SettingsActivity extends BaseActivity {

    @Override
    int getContentViewId() {
        return R.layout.activity_settings;
    }

    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.action_settings;
    }
}