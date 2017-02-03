package com.example.pau.deltacalc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PlayBackFragment extends Fragment {

    private String currentSongText;

    public String getCurrentSongText() {
        return currentSongText;
    }

    public void setCurrentSongText(String currentSongText) {
        this.currentSongText = currentSongText;
    }

    public PlayBackFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_playback_pager, container, false);

        TextView currentSong = (TextView) rootView.findViewById(R.id.music_current_song);
        currentSong.setText(currentSongText);

        return rootView;
    }
}
