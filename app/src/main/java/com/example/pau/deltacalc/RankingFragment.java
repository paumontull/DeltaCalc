package com.example.pau.deltacalc;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class RankingFragment extends Fragment {

    private List<RankingElem> elem_list = new ArrayList<>();
    private RankingAdapter recyclerAdapter;

    public RankingFragment(){
     //Required empty public constructor
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.rank_reset_op_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_mem_reset:
                RankingOpenHelper rankingOpenHelper = new RankingOpenHelper(getActivity().getApplicationContext());
                rankingOpenHelper.resetTable("Ranking");
                elem_list.clear();
                recyclerAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ranking, container, false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.RecyclerView);
        recyclerAdapter = new RankingAdapter(elem_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);

        updateElemList();

        return v;
    }

    public void updateElemList(){
        elem_list.clear();
        RankingOpenHelper rankingOpenHelper = new RankingOpenHelper(getActivity().getApplicationContext());
        Cursor c = rankingOpenHelper.getRanking();
        if(c.moveToFirst()){
            elem_list.add(new RankingElem(c.getString(c.getColumnIndex("user")), c.getInt(c.getColumnIndex("score"))));
            while(c.moveToNext()) elem_list.add(new RankingElem(c.getString(c.getColumnIndex("user")), c.getInt(c.getColumnIndex("score"))));
        }
        recyclerAdapter.notifyDataSetChanged();
    }
}
