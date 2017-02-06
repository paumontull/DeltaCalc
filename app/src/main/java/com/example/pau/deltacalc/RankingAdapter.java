package com.example.pau.deltacalc;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.MyViewHolder> {

    private List<RankingElem> elem_list;

    public RankingAdapter(List<RankingElem> elem_list){
        this.elem_list = elem_list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView userName;
        public TextView userScore;

        public MyViewHolder(View itemView){
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.VH_user_name);
            userScore = (TextView) itemView.findViewById(R.id.VH_user_score);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_elem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RankingElem current = elem_list.get(position);
        holder.userName.setText(current.getUser());
        holder.userScore.setText(String.format(Locale.getDefault(), "%d", current.getScore()));
    }

    @Override
    public int getItemCount() {
        return elem_list.size();
    }
}
