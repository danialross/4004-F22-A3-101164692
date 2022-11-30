package com.example.A3.model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;

public class Crazy8Game {
    private ArrayList<String> deck = new ArrayList<>();
    private ArrayList<Player> players;
    private String currentTopCard;
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
    //used to show the card player just drew
    public String showLastCard(Player p){
        return p.getHand().get(p.getHand().size()-1) + " was drew";
    }

    public void dealPlayerCards(){
        for(int i = 0;i<players.size();i++){
            for(int j = 0;j<5;j++){
                playerDrawCard(players.get(i),null);
            }
        }
    }

    public void playCard(String card){
        if(card.equals("AD") || card.equals("AC") || card.equals("AH") || card.equals("AS")){
            direction = -direction;
        }else if(card.equals("QD") || card.equals("QC") || card.equals("QH") || card.equals("QS")) {
            getNextPlayer();
            getNextPlayer();

        }
        currentTopCard = card;

    }

    public void getNextPlayer() {
        if (direction == 1) {
            currPlayerIndex++;
            if (currPlayerIndex > 3) {
                currPlayerIndex -= 4;
            }
        } else {
            currPlayerIndex--;
            if (currPlayerIndex < 0) {
                currPlayerIndex += 4;
            }
        }
    }

    // return current direction as left or right
    public String showDirection(){
        if(direction==1){
            return "Right";
        }else{
            return "Left";
        }
    }

// returns message to notify players
    public String notifyAction(int option){
        String message;
        if(option == 1){
            message = "";
        }else if( option == 2){
            message = "an Ace was played, the direction has been reversed";
        }else if( option == 3){
            message = "a Queen was played, the next player's turn has been skipped";
        }else{
            message = "The suit has changed to " + currentTopCard.charAt(1) + " because an 8 was played";
        }

        return message;
    }
    //8 card was played
    public void changeSuit(String s){
        currentTopCard = currentTopCard.charAt(0)+s;
    }

    public String requestAction(int option){
        if(option == 1){
            return "Select a new suit.(D/C/H/S)";
        }else{
            //only prompt is user has a 2 card to play
            return "A 2 was played. Either play a 2 card or draw 2.(Index of a 2 card/-1)";
        }
    }

    public boolean hasPlayableCard(Player p,String card){
        for(int i = 0;i<p.getHand().size();i++){
            if(p.getHand().get(i).charAt(0) == card.charAt(0)){
                return true;
            }else if(p.getHand().get(i).charAt(1) == card.charAt(1)){
                return true;
            }
        }
        return false;
    }

    public void calculateHand(Player p){

    }


    public String turnStarts(){
        return "Current Card: " + currentTopCard + "\n" + getCurrentPlayerTurn()+ "'s turn";
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

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setCurrPlayerIndex(int currPlayerIndex) {
        this.currPlayerIndex = currPlayerIndex;
    }

    public void setCurrentTopCard(String currentTopCard) {
        this.currentTopCard = currentTopCard;
    }
}
