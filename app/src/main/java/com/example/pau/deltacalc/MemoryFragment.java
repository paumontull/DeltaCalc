package com.example.pau.deltacalc;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MemoryFragment extends Fragment {

    private View v;
    private int mRowCount, mColCount, lastId = -1;
    private ArrayList<Card> cards = new ArrayList<>();

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

        ResponsiveGridView cardGrid = (ResponsiveGridView) v.findViewById(R.id.mem_card_grid);
        mRowCount = cardGrid.getRowCount();
        mColCount = cardGrid.getColCount();
        int nCards = mRowCount * mColCount;

        ArrayList<Integer> ids = new ArrayList<>();

        for(int i = 0; i < nCards/2; ++i){
            View.inflate(getContext(), R.layout.mem_card_elem, cardGrid);
            cardGrid.getChildAt(i).setId(R.id.mem_card + i);

            int id = (int)(Math.random()*(nCards/2));
            while(ids.contains(id)) id = (int)(Math.random()*(nCards/2));
            ids.add(id);

            cards.add(new Card((CardView) cardGrid.getChildAt(i), id));
        }

        ids.clear();

        for(int i = nCards/2; i < nCards; ++i){
            View.inflate(getContext(), R.layout.mem_card_elem, cardGrid);
            cardGrid.getChildAt(i).setId(R.id.mem_card + i);

            int id = (int)(Math.random()*(nCards/2));
            while(ids.contains(id)) id = (int)(Math.random()*(nCards/2));
            ids.add(id);

            cards.add(new Card((CardView) cardGrid.getChildAt(i), id));
        }

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: Save InstanceState
    }

    public void onClick(View v) {
        int id = v.getId() - R.id.mem_card;
        Card card = cards.get(id);
        card.getCardView().setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent)+ id*8000);
        if(lastId != -1){
            Card lastCard = cards.get(lastId);
            if(lastId != id){
                card.getCardView().setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.cardview_light_background));
                lastCard.getCardView().setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.cardview_light_background));
                lastId = -1;
            }
        }
        Log.v("ID", Integer.toString(id) + " " + Integer.toString(lastId));
        lastId = id;
    }
}
