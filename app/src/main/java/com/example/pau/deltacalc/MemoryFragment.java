package com.example.pau.deltacalc;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MemoryFragment extends Fragment {

    private int lastTag;
    private int tries;
    private ArrayList<Integer> ids = new ArrayList<>();
    private ArrayList<Integer> found = new ArrayList<>();
    private View v;

    public MemoryFragment(){
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            //TODO: Restore savedInstanceState
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_memory, container, false);
        resetGame();
        return v;
    }

    private void resetGame(){
        ResponsiveGridView cardGrid = (ResponsiveGridView) v.findViewById(R.id.mem_card_grid);
        lastTag = 0;
        tries = 0;
        int mRowCount = cardGrid.getRowCount();
        int mColCount = cardGrid.getColCount();
        int nCards = mRowCount * mColCount;

        for(int i = 0; i < nCards; ++i){
            int id = (int)(Math.random()*(nCards/2));
            while(Collections.frequency(ids, id) > 1) {
                id = (int) (Math.random() * (nCards / 2));
            }
            ids.add(id);
        }
        for(int i = 0; i < nCards; ++i){
            View.inflate(getContext(), R.layout.mem_card_elem, cardGrid);
            cardGrid.getChildAt(i).setTag(i);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: Save InstanceState
    }

    private class ResetCards extends AsyncTask<Integer, Void, Integer[]> {
        @Override
        protected Integer[] doInBackground(Integer... params) {
            try{
                Thread.sleep(500);
                return params;
            }
            catch (InterruptedException e){
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(Integer[] integer) {
            changeCardColor(integer[0], integer[2]);
            changeCardColor(integer[1], integer[2]);
            super.onPostExecute(integer);
        }
    }

    private void changeCardColor(int tag, int color){
        CardView cardView = (CardView) v.findViewWithTag(tag);
        cardView.setCardBackgroundColor(color);
    }

    private void killCard(int tag){
        CardView cardView = (CardView) v.findViewWithTag(tag);
        cardView.setVisibility(View.GONE);
    }

    public void onClick(View v) {
        int tag = (int) v.getTag();
        if(!found.contains(tag) && tag != lastTag) {
            changeCardColor(tag, ContextCompat.getColor(getContext(), R.color.colorAccent) + ids.get(tag) * 2000);
            if (lastTag != -1) {
                changeCardColor(lastTag, ContextCompat.getColor(getContext(), R.color.colorAccent) + ids.get(lastTag) * 2000);
                if ((int) ids.get(lastTag) != ids.get(tag)) {
                    new ResetCards().execute(tag, lastTag, ContextCompat.getColor(getContext(), R.color.cardview_light_background));
                }
                else{
                    found.add(tag);
                    found.add(lastTag);
                }
                lastTag = -1;
            } else lastTag = tag;
        }
    }
}
