package com.example.musicplayer.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.SettingsAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class FragmentSettingsBottomSheet extends BottomSheetDialogFragment {
    private final SettingsAdapter.SettingsClickListener mListener;
    private final String[] mSettingsList;

    public FragmentSettingsBottomSheet(String[] list, SettingsAdapter.SettingsClickListener listener) {
        mSettingsList = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet, null);
        bottomDialog.setContentView(view);

        RecyclerView rvPlaylist = view.findViewById(R.id.rvBottomSheet);
        rvPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));

        SettingsAdapter adapter = new SettingsAdapter(mSettingsList, mListener);

        rvPlaylist.setAdapter(adapter);
        return bottomDialog;
    }
}
