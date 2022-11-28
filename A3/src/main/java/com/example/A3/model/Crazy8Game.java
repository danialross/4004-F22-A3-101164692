package com.example.A3.model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;

public class Crazy8Game {
    private ArrayList<String> deck = new ArrayList<>();
    private ArrayList<String> players;
    private String currentTopCard;
    private int currPlayerIndex = 0;
    private int direction = 1;

    public Crazy8Game(ArrayList<String> players){
        this.players = players;
        generateDeck();
        Collections.shuffle(deck);
    }

    public void generateDeck(){
        String[] suit = {"D","C","H","S"};
        String[] rank = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};

        for(int j = 0; j < 13; j++){
            for(int i = 0; i < 4; i++){
                String c = rank[j]+suit[i];
                this.deck.add(c);
            }
        }
    }
//
//    public Card drawCard(Card rigCard){
//        if( deck.contains(rigCard)){
//            deck.remove(rigCard)
//        }
//    }

    public String getCurrentTopCard() {
        return currentTopCard;
    }

    public String getCurrentPlayerTurn(){
        return players.get(currPlayerIndex);
    }

    public ArrayList<String> getDeck() {
        return deck;
    }
}
