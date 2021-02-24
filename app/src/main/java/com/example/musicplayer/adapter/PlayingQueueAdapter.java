package com.example.musicplayer.adapter;

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

public class PlayingQueueAdapter extends RecyclerView.Adapter<PlayingQueueAdapter.SongViewHolder> {
    private final PlayingQueueClickListener mListener;
    private ArrayList<Song> mSongList;

    public PlayingQueueAdapter(ArrayList<Song> list, PlayingQueueClickListener listener) {
        this.mSongList = list;
        mListener = listener;
    }

    public void setSongList(ArrayList<Song> songList) {
        mSongList = songList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playing_queue, parent, false);
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

    public interface PlayingQueueClickListener {
        void onQueuedSongSelected(int position);
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitle;
        private final TextView txtArtist;
        private final ImageView imgAlbumCover;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtArtist = itemView.findViewById(R.id.txtSongsCount);
            imgAlbumCover = itemView.findViewById(R.id.imgAlbumCover);
        }

        public void setOnItemCLick(int position, PlayingQueueClickListener mListener) {
            itemView.setOnClickListener(view -> mListener.onQueuedSongSelected(position));
        }
    }
}
