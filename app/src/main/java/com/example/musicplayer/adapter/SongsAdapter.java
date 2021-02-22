package com.example.musicplayer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.model.Song;

import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {
    private final Context mContext;
    private final ArrayList<Song> mSongList;
    private final OnItemClickListener mListener;

    public SongsAdapter(Context context, ArrayList<Song> list, OnItemClickListener listener) {
        this.mContext = context;
        this.mSongList = list;
        mListener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = mSongList.get(position);
        if (song == null) {
            return;
        }
        holder.txtTitle.setText(song.getTitle());
        holder.txtArtist.setText(song.getArtist());
        if (song.getAlbumCover() != null) {
            holder.imgAlbumCover.setImageDrawable(song.getAlbumCover());
            Log.i("=====LOG ", song.getAlbumCover().toString());
        }
        holder.setOnItemCLick(position, mListener);
    }

    @Override
    public int getItemCount() {
        if (mSongList != null) {
            return mSongList.size();
        }
        return 0;
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitle;
        private final TextView txtArtist;
        private final ImageView imgAlbumCover;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtArtist = itemView.findViewById(R.id.txtArtist);
            imgAlbumCover = itemView.findViewById(R.id.imgAlbumCover);
        }

        public void setOnItemCLick(int position, OnItemClickListener mListener) {
            itemView.setOnClickListener(view -> mListener.onItemClickListener(position));
        }
    }
}
