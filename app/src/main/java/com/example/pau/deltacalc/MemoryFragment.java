package com.example.pau.deltacalc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MemoryFragment extends Fragment {

    private View v;
    private int mRowCount, mColCount;

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

        for(int i = 0; i < nCards; ++i){
            View.inflate(getContext(), R.layout.mem_card_elem, cardGrid);
        }

        cardGrid.getChildAt(1).setBackground(getResources().getDrawable(R.color.colorAccent));

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: Save InstanceState
    }

    public void onClick(View v){

        Toast.makeText(v.getContext(), "click" + Integer.toString(v.getId()), Toast.LENGTH_SHORT).show();

        switch(v.getId()){
            //TODO: Handle click events
        }
    }


}
