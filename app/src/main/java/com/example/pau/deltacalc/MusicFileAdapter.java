package com.example.pau.deltacalc;

import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.media.MediaPlayer;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class MusicFileAdapter extends RecyclerView.Adapter<MusicFileAdapter.MyViewHolder>{
    private List<File> button_list;

    public MusicFileAdapter(List<File> button_list){
        this.button_list = button_list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        MediaPlayer mediaPlayer = new MediaPlayer();
        public TextView fileName;
        public FrameLayout frame;
        private File song;

        public MyViewHolder(View itemView) {
            super(itemView);

            fileName = (TextView) itemView.findViewById(R.id.VH_file_name);
            frame = (FrameLayout) itemView.findViewById(R.id.VH_card_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.VH_card_view:
                    song = button_list.get(getAdapterPosition());
                    mediaPlayer.stop();
                    try {
                        mediaPlayer.setDataSource(song.getAbsolutePath());
                        mediaPlayer.prepare();
                    } catch (IllegalArgumentException | IllegalStateException | IOException e) {
                        Log.v("Bad file: ", song.getAbsolutePath());
                    }
                    mediaPlayer.start();
                    break;
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_file_elem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        File soundFile = button_list.get(position);
        holder.fileName.setText(soundFile.getName());
    }

    @Override
    public int getItemCount() {
        return button_list.size();
    }
}
