package com.example.pau.deltacalc;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import rm.com.youtubeplayicon.PlayIconDrawable;

public class MusicFragment extends Fragment implements View.OnClickListener, OnRecyclerViewClickListener, ViewPager.OnPageChangeListener{

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

    private List<File> button_list = new ArrayList<>();
    private ViewPager playBackPager;
    private PagerAdapter pagerAdapter;
    private PlayIconDrawable play;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Boolean prepared = false;

    public MusicFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_music, container, false);

        playBackPager = (ViewPager) v.findViewById(R.id.music_playback_pager);
        pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        playBackPager.setAdapter(pagerAdapter);
        playBackPager.addOnPageChangeListener(this);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.RecyclerView);
        MusicFileAdapter recyclerAdapter = new MusicFileAdapter(this, button_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);

        ImageView iconView = (ImageView) v.findViewById(R.id.music_play_pause);
        play = PlayIconDrawable.builder()
                .withColor(Color.WHITE)
                .withInterpolator(new FastOutSlowInInterpolator())
                .withDuration(300)
                .withInitialState(PlayIconDrawable.IconState.PLAY)
                .into(iconView);

        FrameLayout playPauseContainer = (FrameLayout) v.findViewById(R.id.music_play_pause_container);
        playPauseContainer.setOnClickListener(this);

        button_list.addAll(getPaths(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)));

        return v;
    }

    private void setPlayBackPager(List<File> queue){
        for(int i = 0; i < queue.size(); ++i){
            PlayBackFragment fragment = new PlayBackFragment();
            String currentName = queue.get(i).getName();
            fragment.setCurrentSongText(currentName.substring(0, currentName.lastIndexOf(".")));
            pagerAdapter.addFragment(fragment);
        }
    }

    private List<File> getPaths(File root){
        List<File> inputFiles = new ArrayList<>(Arrays.asList(root.listFiles()));
        List<File> outputFiles = new ArrayList<>(Arrays.asList(root.listFiles()));
        for(File file : inputFiles){
            String name = file.getName();
            if(file.isDirectory()){
                outputFiles.addAll(getPaths(file));
                outputFiles.remove(file);
            }
            else if(!formats.contains(name.substring(name.lastIndexOf(".")))){
                outputFiles.remove(file);
            }
        }
        return outputFiles;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.music_play_pause_container:
                play.toggle(true);
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    play.animateToState(PlayIconDrawable.IconState.PLAY);
                }
                else{
                    setPlayBackPager(button_list);
                    play.animateToState(PlayIconDrawable.IconState.PAUSE);
                }
                break;
        }
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        if(!prepared){
            setPlayBackPager(button_list);
            prepared = true;
        }
        playBackPager.setCurrentItem(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        if(!prepared)play.animateToState(PlayIconDrawable.IconState.PAUSE);
        else play.setIconState(PlayIconDrawable.IconState.PAUSE);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(button_list.get(position).getAbsolutePath());
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playBackPager.setCurrentItem(playBackPager.getCurrentItem() + 1);
                }
            });
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (IllegalArgumentException | IllegalStateException | IOException e) {
            Log.v("Bad file: ", button_list.get(position).getAbsolutePath());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    public class PagerAdapter extends FragmentStatePagerAdapter {

        private List<PlayBackFragment> fragments;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            notifyDataSetChanged();
        }

        public void addFragment(PlayBackFragment fragment){
            this.fragments.add(fragment);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
