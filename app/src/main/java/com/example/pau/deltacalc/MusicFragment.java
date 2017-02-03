package com.example.pau.deltacalc;

import android.graphics.Color;
import android.graphics.Rect;
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
import java.util.HashSet;
import java.util.List;

import rm.com.youtubeplayicon.PlayIconDrawable;


/**
 * A simple {@link Fragment} subclass.
 */
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
    private MusicFileAdapter RecyclerAdapter;
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
        RecyclerAdapter = new MusicFileAdapter(this, button_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(24));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(RecyclerAdapter);

        ImageView iconView = (ImageView) v.findViewById(R.id.music_play_pause);
        play = PlayIconDrawable.builder()
                .withColor(Color.WHITE)
                .withInterpolator(new FastOutSlowInInterpolator())
                .withDuration(300)
                .withInitialState(PlayIconDrawable.IconState.PLAY)
                .into(iconView);

        FrameLayout playPauseContainer = (FrameLayout) v.findViewById(R.id.music_play_pause_container);
        playPauseContainer.setOnClickListener(this);

        getPaths();

        return v;
    }

    private void setPlayBackPager(List<File> queue){
        for(int i = 0; i < queue.size(); ++i){
            PlayBackFragment fragment = new PlayBackFragment();
            fragment.setCurrentSongText(queue.get(i).getName());
            pagerAdapter.addFragment(fragment);
        }
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
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    play.animateToState(PlayIconDrawable.IconState.PLAY);
                }
                else{
                    mediaPlayer.start();
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
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (IllegalArgumentException | IllegalStateException | IOException e) {
            Log.v("Bad file: ", button_list.get(position).getAbsolutePath());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

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
