package com.example.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.database.PlaylistDatabase;
import com.example.musicplayer.model.Playlist;

import java.util.ArrayList;

public class PlaylistsAdapter extends RecyclerView.Adapter<PlaylistsAdapter.PlaylistViewHolder> {
    private final PlaylistClickListener mListener;
    private ArrayList<Playlist> mPlaylist;
    private final Context mContext;
    private int mSongCount;

    public PlaylistsAdapter(Context context, PlaylistClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setPlaylist(ArrayList<Playlist> playlistList) {
        mPlaylist = playlistList;
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
        int playlistId = playlist.getId();
        holder.txtTitle.setText(playlist.getTitle());
        String countString = "";
        mSongCount = PlaylistDatabase.getInstance(mContext).playlistSongDAO()
                .getSongsCountByPlaylist(playlistId);
        if (mSongCount == 1 || mSongCount == 0) {
            countString = mSongCount + " Song";
        } else {
            countString = mSongCount + " Songs";
        }
        holder.txtSongsCount.setText(countString);
        holder.itemView.setOnClickListener(v -> mListener.onPlaylistSelected(playlistId));
    }

    @Override
    public int getItemCount() {
        if (mPlaylist != null) {
            return mPlaylist.size();
        }
        return 0;
    }

    public interface PlaylistClickListener {
        void onPlaylistSelected(int playlistId);
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitle;
        private final TextView txtSongsCount;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtSongsCount = itemView.findViewById(R.id.txtSongsCount);
        }
    }
}
