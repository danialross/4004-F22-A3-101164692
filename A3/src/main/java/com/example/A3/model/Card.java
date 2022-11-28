package com.example.A3.model;

public class Card {
    private String suit;
    private String rank;

    public Card(String suit,String rank){
        this.suit = suit;
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank+suit;
    }
}
