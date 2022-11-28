package com.example.A3.model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;

public class Crazy8Game {
    private ArrayList<Card> deck = new ArrayList<>();
    private ArrayList<String> players;
    private Card currentTopCard;
    private int currPlayerIndex = 0;
    private int direction = 1;

    public Crazy8Game(ArrayList<String> players){
        this.players = players;
        generateDeck();
        Collections.shuffle(deck);
    }

    public void generateDeck(){
        
    }

    public Card getCurrentTopCard() {
        return currentTopCard;
    }

    public String getCurrentPlayerTurn(){
        return players.get(currPlayerIndex);
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }
}
