package com.example.A3.model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;

public class Crazy8Game {
    private ArrayList<String> deck = new ArrayList<>();
    private ArrayList<Player> players;
    private String currentTopCard;
    private String specialCardPlayed;
    private int currPlayerIndex = 0;
    private int direction = 1;

    public Crazy8Game(ArrayList<Player> players){
        this.players = players;
        generateDeck();
        Collections.shuffle(deck);
    }

    // when card is taken from draw deck and place in the middle
    public String drawCard(String riggedCard){

        if(riggedCard != null){
            if(deck.remove(riggedCard)){
                return riggedCard;
            }else{
                return null;
            }

        }else{
            return deck.remove(0);
        }

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

    //player draws a card and places in hand
    public void playerDrawCard(Player p,String riggedCard){

        ArrayList<String> newHand;
        if(p.getHand() == null){
            newHand = new ArrayList<>();
        }else{
            newHand = p.getHand();
        }
        if(riggedCard != null){
            newHand.add(drawCard(riggedCard));
        }else{
            newHand.add(drawCard(null));
        }
        p.setHand(newHand);

    }

    public void dealPlayerCards(){
        for(int i = 0;i<players.size();i++){
            for(int j = 0;j<5;j++){
                playerDrawCard(players.get(i),null);
            }
        }
    }

    public void playCard(String card){
        return;
    }
    public String getCurrentTopCard() {
        return currentTopCard;
    }

    public String getCurrentPlayerTurn(){
        return players.get(currPlayerIndex).getName();
    }

    public ArrayList<String> getDeck() {
        return deck;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getDirection() {
        return direction;
    }

    public String getSpecialCardPlayed() {
        return specialCardPlayed;
    }
}
