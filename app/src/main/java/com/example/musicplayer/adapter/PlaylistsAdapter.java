package com.example.musicplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.model.Playlist;

import java.util.ArrayList;

public class PlaylistsAdapter extends RecyclerView.Adapter<PlaylistsAdapter.PlaylistViewHolder> {
    private final PlaylistClickListener mListener;
    private ArrayList<Playlist> mPlaylist;

    public PlaylistsAdapter(PlaylistClickListener listener) {
        mListener = listener;
    }

    public void setPlaylist(ArrayList<Playlist> playlist) {
        mPlaylist = playlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = mPlaylist.get(position);
        if (playlist == null) {
            return;
        }
        holder.txtTitle.setText(playlist.getTitle());
        String countString = "";
        int count = playlist.getCount();
        if (count == 1 || count == 0) {
            countString = count + " Song";
        } else {
            countString = count + " Songs";
        }
        holder.txtSongsCount.setText(countString);
        holder.setOnItemCLick(position, mListener);
    }

    @Override
    public int getItemCount() {
        if (mPlaylist != null) {
            return mPlaylist.size();
        }
        return 0;
    }

    public interface PlaylistClickListener {
        void onPlaylistSelected(int position);
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitle;
        private final TextView txtSongsCount;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtSongsCount = itemView.findViewById(R.id.txtSongsCount);
        }

        public void setOnItemCLick(int position, PlaylistClickListener mListener) {
            itemView.setOnClickListener(view -> mListener.onPlaylistSelected(position));
        }
    }
}
