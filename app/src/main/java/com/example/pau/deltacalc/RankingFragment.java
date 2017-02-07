package com.example.pau.deltacalc;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class RankingFragment extends Fragment {

    public RankingFragment(){
        //Required empty public constructor
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private PagerAdapter pagerAdapter;
    private LinearLayout noItems;
    private ViewPager rankingPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ranking, container, false);

        rankingPager = (ViewPager) v.findViewById(R.id.rank_pager);
        pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        rankingPager.setAdapter(pagerAdapter);

        noItems = (LinearLayout) v.findViewById(R.id.rank_no_items);

        updateElemList();

        return v;
    }

    public void updateElemList(){
        boolean hasData = false;
        RankingOpenHelper rankingOpenHelper = new RankingOpenHelper(getActivity().getApplicationContext());
        Integer[] cards = {64, 56, 48, 42, 40, 36, 32, 30, 28, 24, 20, 18, 16, 14, 12, 10, 8, 6, 4};
        for(int i : cards){
            Cursor c = rankingOpenHelper.getRanking(i);
            if(c.getCount() > 0){
                hasData = true;
                List<RankingElem> elemList = new ArrayList<>();
                RankingPageFragment fragment = new RankingPageFragment();
                fragment.setCards(i);
                if(c.moveToFirst()){
                    elemList.add(new RankingElem(c.getString(c.getColumnIndex("user")), c.getInt(c.getColumnIndex("score"))));
                    while(c.moveToNext()) elemList.add(new RankingElem(c.getString(c.getColumnIndex("user")), c.getInt(c.getColumnIndex("score"))));
                }
                fragment.setElemList(elemList);
                pagerAdapter.addFragment(fragment);
            }
        }
        if(!hasData){
            noItems.setVisibility(View.VISIBLE);
            rankingPager.setVisibility(View.GONE);
        }
        else{
            noItems.setVisibility(View.GONE);
            rankingPager.setVisibility(View.VISIBLE);
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        private List<RankingPageFragment> fragments;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            notifyDataSetChanged();
        }

        public void addFragment(RankingPageFragment fragment){
            this.fragments.add(fragment);
            notifyDataSetChanged();
        }

        public void clearFragments(){
            if(!this.fragments.isEmpty()){
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                for(RankingPageFragment fragment : this.fragments){
                    ft.remove(fragment);
                }
                ft.commitAllowingStateLoss();
            }
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
                pagerAdapter.clearFragments();
                noItems.setVisibility(View.VISIBLE);
                rankingPager.setVisibility(View.GONE);
                break;
        }
        return true;
    }
}
