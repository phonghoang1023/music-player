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

    public FragmentSettingsBottomSheet(SettingsAdapter.SettingsClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet, null);
        bottomDialog.setContentView(view);

        RecyclerView rvPlaylist = view.findViewById(R.id.rvPlaylist);
        rvPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));
        String[] settingsList = {
                getString(R.string.add_to_queue),
                getString(R.string.add_to_playlist),
                getString(R.string.add_to_favorite),
                getString(R.string.delete_from_device)};

        SettingsAdapter adapter = new SettingsAdapter(settingsList, position -> {
            mListener.onSettingSelected(position);
        });

        rvPlaylist.setAdapter(adapter);
        return bottomDialog;
    }
}
