package com.example.pau.deltacalc;


import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.jar.Manifest;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment {

    private List<AudioFile> button_list = new ArrayList<>();
    private RecyclerView recyclerView;
    private AudioFileAdapter adapter;
    private Toolbar toolbar;

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

        recyclerView = (RecyclerView) v.findViewById(R.id.RecyclerView);

        adapter = new AudioFileAdapter(button_list);

        int nCols;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) nCols = 3;
        else nCols = 5;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(v.getContext(), nCols);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(4));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getPaths();

        return v;
    }

    private File[] getPaths(){
        File musicRoot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File[] files = musicRoot.listFiles();
        Log.v("FILE:", musicRoot.getAbsolutePath());
        for(File file : files){
            String name = file.getName();
            if(file.isDirectory()){

            }
            else if(formats.contains(name.substring(name.lastIndexOf(".")))){
                button_list.add(new AudioFile(file));
            }
        }
        return files;
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space/2;
            outRect.right = space/2;
            outRect.bottom = space;
        }
    }

}
