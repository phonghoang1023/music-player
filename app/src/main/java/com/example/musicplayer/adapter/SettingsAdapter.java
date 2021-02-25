package com.example.musicplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;


public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {
    private final String[] mList;
    private final SettingsClickListener mListener;

    public SettingsAdapter(String[] list, SettingsClickListener listener) {
        this.mList = list;
        mListener = listener;
    }

    @NonNull
    @Override
    public SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings_action, parent, false);
        return new SettingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder holder, int position) {
        String settingTitle = mList[position];
        if (settingTitle != null) {
            holder.txtSetting.setText(settingTitle);
            holder.itemView.setOnClickListener(v -> mListener.onSettingSelected(position));
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.length;
        }
        return 0;
    }

    public interface SettingsClickListener {
        void onSettingSelected(int position);
    }

    public class SettingsViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtSetting;

        public SettingsViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSetting = itemView.findViewById(R.id.txtSetting);
        }
    }
}
