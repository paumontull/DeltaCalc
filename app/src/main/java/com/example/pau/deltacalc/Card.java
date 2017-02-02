package com.example.pau.deltacalc;

import android.support.v7.widget.CardView;

public class Card {

    private CardView cardView;
    private int id;

    public Card(CardView cardView, int id){
        this.cardView = cardView;
        this.id = id;
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
