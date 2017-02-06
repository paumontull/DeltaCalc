package com.example.pau.deltacalc;

public class RankingElem {

    private String user;
    private int score;

    public RankingElem(String user, int score){
        this.user = user;
        this.score = score;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
