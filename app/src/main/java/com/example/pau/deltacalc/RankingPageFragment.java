package com.example.pau.deltacalc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RankingPageFragment extends Fragment {

    private List<RankingElem> elemList = new ArrayList<>();
    private int cards;

    public void setElemList(List<RankingElem> elemList) {
        this.elemList = elemList;
    }

    public void setCards(int cards) {
        this.cards = cards;
    }

    public RankingPageFragment(){
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_ranking_pager, container, false);

        TextView cardsNum = (TextView) v.findViewById(R.id.rank_pager_cards);
        cardsNum.setText(String.format(Locale.getDefault(), "%d", cards));

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.RecyclerView);
        RankingAdapter recyclerAdapter = new RankingAdapter(elemList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);

        return v;
    }
}
