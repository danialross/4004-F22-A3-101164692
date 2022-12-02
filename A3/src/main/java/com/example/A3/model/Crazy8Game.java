package com.example.A3.model;

import java.util.ArrayList;
import java.util.Collections;

public class Crazy8Game {
    private ArrayList<String> deck = new ArrayList<>();
    private final ArrayList<Player> players;
    private String currentTopCard;
    private int currPlayerIndex = 0;
    private int direction = 1;
    private final int winningThreshold = 100;
    private boolean plus2Played = false;

    public Crazy8Game(ArrayList<Player> players){
        this.players = players;
        generateDeck();
        Collections.shuffle(deck);
    }

    // when card is taken from draw deck
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

    // dealer draws a card and places in topcard
    public void dealerDrawCard(String riggedCard){
        if(riggedCard != null){
            currentTopCard = drawCard(riggedCard);
        }else{
            currentTopCard = drawCard(null);
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
        for (Player player : players) {
            for (int j = 0; j < 5; j++) {
                playerDrawCard(player, null);
            }
        }
    }

    public void playCard(String card){
        if(card.charAt(0)=='A'){
            direction = -direction;
        }else if(card.charAt(0)=='Q') {
            turnFinished();
        }else if(card.charAt(0)=='2'){
            plus2Played = true;
        }
        currentTopCard = card;
        turnFinished();


    }

    // player plays another 2 card after a 2 was played, so he does not +2 cards but the next player does
    public void respondWith2Card(boolean response,String[] riggedCard){
        if(!response){
            if(riggedCard != null){
                playerDrawCard(players.get(currPlayerIndex),riggedCard[0]);
                playerDrawCard(players.get(currPlayerIndex),riggedCard[1]);
            }else{
                playerDrawCard(players.get(currPlayerIndex),null);
                playerDrawCard(players.get(currPlayerIndex),null);
            }
            plus2Played = false;
        }

        turnFinished();

    }

    public void turnFinished() {
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
            return "Left";
        }else{
            return "Right";
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
        if(plus2Played){
            for(String playerCard: p.getHand()){
                if(playerCard.charAt(0) == '2'){
                    return true;
                }
            }
        }else{
            for(int i = 0;i<p.getHand().size();i++){
                if(p.getHand().get(i).charAt(0) == '8'){
                    return true;
                }else if(p.getHand().get(i).charAt(0) == card.charAt(0)){
                    return true;
                }else if(p.getHand().get(i).charAt(1) == card.charAt(1)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isRoundUnplayable(){
        for (Player player : players) {
            if (hasPlayableCard(player, currentTopCard)) {
                return false;
            }
        }
        if(deck.size()>0) {
            return false;
        }
        return true;
    }

    public void calculateHand(Player p){
        int total = 0;
        for(int i = 0; i<p.getHand().size(); i++){
            if(p.getHand().get(i).charAt(0) == 'A'){
                total += 1;
            }else if(p.getHand().get(i).charAt(0) == '8'){
                total += 50;
            }else if(p.getHand().get(i).charAt(0) == '1' ||
                    p.getHand().get(i).charAt(0) == 'J' ||
                    p.getHand().get(i).charAt(0) == 'Q' ||
                    p.getHand().get(i).charAt(0) == 'K'){
                total += 10;
            }else{
                    total += Integer.parseInt(String.valueOf(p.getHand().get(i).charAt(0)));
                }
            }
        p.setScore(p.getScore()+total);
    }

    public boolean isDrawDeckEmpty(){
        return deck.size() == 0;
    }

    public boolean didReachWinningThreshold(){
        for (Player player : players) {
            if (player.getScore() >= winningThreshold) {
                return true;
            }
        }
        return false;
    }
    public Boolean didFinishRound(){
        for (Player player : players) {
            if (player.getHand().size() == 0) {
                return true;
            }
        }
        return false;
    }

    public Player getWinner(){
        Player currentLowestScore = players.get(0);
        for(int i = 1; i<players.size();i++){
            if(players.get(i).getScore()<currentLowestScore.getScore()){
                currentLowestScore = players.get(i);
            }
        }
        return currentLowestScore;
    }

    public String showScores(){
        return "--Player scores--\n" +
                players.get(0).getName()+" : " + players.get(0).getScore() +" Points\n" +
                players.get(1).getName()+" : " + players.get(1).getScore() +" Points\n" +
                players.get(2).getName()+" : " + players.get(2).getScore() +" Points\n" +
                players.get(3).getName()+" : " + players.get(3).getScore() +" Points\n";
    }

    public String showCurrentPlayersTurn(){
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

    public void setDeck(ArrayList<String> deck) {
        this.deck = deck;
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

    public int getCurrPlayerIndex() {
        return currPlayerIndex;
    }

    public void setPlus2Played(boolean plus2Played) {
        this.plus2Played = plus2Played;
    }

    public boolean isPlus2Played() {
        return plus2Played;
    }
}
