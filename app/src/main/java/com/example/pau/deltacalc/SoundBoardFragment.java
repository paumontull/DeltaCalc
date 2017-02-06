package com.example.pau.deltacalc;


import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;



public class SoundBoardFragment extends Fragment {

    private List<File> button_list = new ArrayList<>();

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

    public SoundBoardFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sound, container, false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.RecyclerView);

        SoundFileAdapter adapter = new SoundFileAdapter(button_list);

        int nCols;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) nCols = 3;
        else nCols = 5;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(v.getContext(), nCols);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(4));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        button_list.addAll(getPaths(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)));

        return v;
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
