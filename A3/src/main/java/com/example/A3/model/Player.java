package com.example.A3.model;

import java.security.Principal;
import java.util.ArrayList;

public class Player {
    private Principal user;
    private String name;
    private ArrayList<String> hand;
    private int score;

    public Player(Principal user, String name){
        this.user = user;
        this.name = name;
        score = 0;
    }

    public Principal getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getHand() {
        return hand;
    }

    public void setHand(ArrayList<String> hand) {
        this.hand = hand;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
