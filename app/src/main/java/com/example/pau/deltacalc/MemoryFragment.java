package com.example.pau.deltacalc;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MemoryFragment extends Fragment implements OnEndGameResetButtonClicked {

    private int lastTag;
    private int tries;
    private int nCards;
    private TextView triesText;
    private ArrayList<Integer> ids;
    private ArrayList<Integer> found;
    private View v;

    public MemoryFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            //TODO: Restore savedInstanceState
        }
        setGame();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_memory, container, false);
        triesText = (TextView) v.findViewById(R.id.mem_tries);
        return v;
    }

    private void setGame(){
        ResponsiveGridView cardGrid = (ResponsiveGridView) v.findViewById(R.id.mem_card_grid);
        cardGrid.removeAllViews();
        lastTag = -1;
        tries = 0;
        ids = new ArrayList<>();
        found = new ArrayList<>();
        int mRowCount = cardGrid.getRowCount();
        int mColCount = cardGrid.getColCount();
        nCards = mRowCount * mColCount;

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

    private class KillCards extends AsyncTask<Integer, Void, Integer[]> {
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
            for(Integer tag : integer) killCard(tag);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mem_reset_op_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_mem_reset:
                setGame();
                break;
        }
        return true;
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
                    new KillCards().execute(tag, lastTag);
                }
                lastTag = -1;
                triesText.setText(String.format(Locale.getDefault(), "%d", ++tries));
            } else lastTag = tag;
        }
        if(found.size() == nCards) endOfGame();
    }

    private void endOfGame(){
        RankingOpenHelper rankingOpenHelper = new RankingOpenHelper(getActivity().getApplicationContext());
        ContentValues values = new ContentValues();
        values.put("user", "Pau");
        values.put("score", String.format(Locale.getDefault(), "%d", tries));
        rankingOpenHelper.addScore(values, "Ranking");

        EndOfGameDialogFragment endOfGameDialogFragment = new EndOfGameDialogFragment();
        endOfGameDialogFragment.setTries(tries);
        endOfGameDialogFragment.setListener(this);
        endOfGameDialogFragment.show(getFragmentManager(), "End of game");
    }

    @Override
    public void onEndGameResetButtonClicked() {
        setGame();
    }

    public static class EndOfGameDialogFragment extends DialogFragment{
        private int tries;
        private OnEndGameResetButtonClicked listener;

        public void setListener(OnEndGameResetButtonClicked listener) {
            this.listener = listener;
        }

        public void setTries(int tries){
            this.tries = tries;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("You beat the game in " + String.format(Locale.getDefault(), "%d", tries) + " tries.")
                    .setPositiveButton(R.string.nav_mem_reset_game, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            listener.onEndGameResetButtonClicked();
                        }
                    });
            return builder.create();
        }
    }
}
