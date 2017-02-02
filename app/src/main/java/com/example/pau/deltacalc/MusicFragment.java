package com.example.pau.deltacalc;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import rm.com.youtubeplayicon.PlayIcon;
import rm.com.youtubeplayicon.PlayIconDrawable;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment implements View.OnClickListener{

    private List<File> button_list = new ArrayList<>();
    private PlayIconDrawable play;

    private static HashSet<String> formats = new HashSet<String>(){{
        add(".3gp");
        add(".mp4");
        add(".m4a");
        add(".aac");
        add(".ts");
        add(".flac");
        add(".mid");
        add(".xmf");
        add(".mxmf");
        add(".rtttl");
        add(".rtx");
        add(".ota");
        add(".imy");
        add(".mp3");
        add(".mkv");
        add(".wav");
        add(".ogg");
    }};

    public MusicFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_music, container, false);

        FrameLayout playPauseContainer = (FrameLayout) v.findViewById(R.id.music_play_pause_container);

        ImageView iconView = (ImageView) v.findViewById(R.id.music_play_pause);
        play = PlayIconDrawable.builder()
                .withColor(Color.WHITE)
                .withInterpolator(new FastOutSlowInInterpolator())
                .withDuration(300)
                .withInitialState(PlayIconDrawable.IconState.PLAY)
                .into(iconView);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.RecyclerView);

        MusicFileAdapter adapter = new MusicFileAdapter(button_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(24));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getPaths();

        playPauseContainer.setOnClickListener(this);

        return v;
    }

    private File[] getPaths(){
        File musicRoot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File[] files = musicRoot.listFiles();
        Log.v("FILE:", musicRoot.getAbsolutePath());
        for(File file : files){
            String name = file.getName();
            if(file.isDirectory()){
                //TODO: recursively look in directory
            }
            else if(formats.contains(name.substring(name.lastIndexOf(".")))){
                button_list.add(file);
            }
        }
        return files;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.music_play_pause_container:
                play.toggle(true);
                break;
            default:
                break;
        }
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = space;
        }
    }

}
