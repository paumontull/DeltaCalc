package com.example.pau.deltacalc;

import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.IOException;
import java.util.List;


public class AudioFileAdapter extends RecyclerView.Adapter<AudioFileAdapter.MyViewHolder>{
    private List<AudioFile> button_list;

    public AudioFileAdapter(List<AudioFile> button_list){
        this.button_list = button_list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener{

        MediaPlayer mediaPlayer = new MediaPlayer();
        public TextView fileName;
        public CardView cardView;
        private AudioFile audioFile;

        public MyViewHolder(View itemView) {
            super(itemView);

            fileName = (TextView) itemView.findViewById(R.id.VH_file_name);
            cardView = (CardView) itemView.findViewById(R.id.VH_card_view);

            itemView.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN && !mediaPlayer.isPlaying()){
                audioFile = button_list.get(getAdapterPosition());
                cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.getContext(), audioFile.getColor()));
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(audioFile.getFile().getAbsolutePath());
                    mediaPlayer.prepare();
                    mediaPlayer.setLooping(true);
                } catch (IllegalArgumentException | IllegalStateException | IOException e) {
                    Log.v("Bad file: ", audioFile.getFile().getAbsolutePath());
                }
                mediaPlayer.start();
            }
            else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                mediaPlayer.reset();
                cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.getContext(),R.color.cardview_light_background));
                return true;
            }
            return false;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_file_elem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AudioFile audioFile = button_list.get(position);
        holder.fileName.setText(audioFile.getName());
    }

    @Override
    public int getItemCount() {
        return button_list.size();
    }
}
