package com.example.A3.model;

import java.util.ArrayList;
import java.util.Collections;

public class Crazy8Game {
    private ArrayList<String> deck = new ArrayList<>();
    private final ArrayList<Player> players;
    private int currentFirstPlayer = 0;
    private String currentTopCard;
    private int currPlayerIndex = 0;
    private int direction = 1;
    private final int winningThreshold = 100;
    private boolean plus2Played = false;
    private int plus2Stack = 0;


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

    public void dealPlayerCards(String[][] playercards){
        for (int i = 0; i < players.size(); i++) {
            for (int j = 0; j < 5; j++) {
                playerDrawCard(players.get(i), playercards[i][j]);
            }
        }
    }

    public void playCard(String card){
        players.get(currPlayerIndex).getHand().remove(card);
        if(card.charAt(0)=='A'){
            direction = -direction;
        }else if(card.charAt(0)=='Q') {
            turnFinished();
        }else if(card.charAt(0)=='2'){
            plus2Played = true;
            plus2Stack += 2;
        }

        if(card.charAt(0)!='2'){
            plus2Played = false;
            plus2Stack = 0;
        }
        currentTopCard = card;
        players.get(currPlayerIndex).setDrawCounter(0);

    }

    // plays a single round
    public void playRound(String card,String[] riggedDraws,String[] respond2Cards){
        if(plus2Played){
            respondWith2Card(respond2Cards,riggedDraws);
            return;
        }

        // null card means player does not have a card to play
        if(card == null){
            drawUpTo3(riggedDraws);

        } else{
            playCard(card);
        }

        turnFinished();
    }


    // player plays another 2 card after a 2 was played, so he does not +2 cards but the next player does
    public void respondWith2Card(String[] cards,String[] riggedCard){
        if(cards == null){

            for(int i=0;i<plus2Stack;i++){
                playerDrawCard(players.get(currPlayerIndex),riggedCard[i]);

            }
        }else{
            if(cards[0].charAt(0)!='2'){
                int stack = plus2Stack;
                playCard(cards[0]);
                playCard(cards[1]);
                plus2Stack = stack+2;
                plus2Played = true;
                turnFinished();
            }else{
                playCard(cards[0]);
                turnFinished();
            }
        }

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

    public void setupNextRound(String firstCard){
        direction = 1;
        plus2Played = false;
        plus2Stack = 0;

        generateDeck();
        Collections.shuffle(deck);
        currentFirstPlayer++;
        if(currentFirstPlayer == 4 ){
            currentFirstPlayer = 0;
        }
        currPlayerIndex = currentFirstPlayer;
        dealerDrawCard(firstCard);
    }

    // return current direction as left or right
    public String showDirection(){
        if(direction==1){
            return "Current Direction is Left";
        }else{
            return "Current Direction is Right";
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

    //if player has a card to play
    public boolean hasPlayableCard(Player p){
        if(plus2Played){
            if(p.getHand().size()<2){
                return false;
            }else{
                int counter = 0;
                for(int i = 0;i<p.getHand().size();i++){
                    if(p.getHand().get(i).charAt(0) == '8' ||
                            p.getHand().get(i).charAt(0) == currentTopCard.charAt(0) ||
                            p.getHand().get(i).charAt(1) == currentTopCard.charAt(1)) {

                        counter++;
                    }

                    if(counter==2){
                        return true;
                    }
                }
            }

        }else{
            for(int i = 0;i<p.getHand().size();i++){
                if(p.getHand().get(i).charAt(0) == '8' ||
                        p.getHand().get(i).charAt(0) == currentTopCard.charAt(0) ||
                        p.getHand().get(i).charAt(1) == currentTopCard.charAt(1)){

                    return true;
                }
            }
        }
        return false;
    }

    //input validation for user
    public boolean canPlay(String card){
        if(!players.get(currPlayerIndex).getHand().contains(card)){
            return false;
        }

        if(card.charAt(0)=='8'){
            return true;
        }
        if(card.charAt(0)==currentTopCard.charAt(0) ){
            return true;
        }
        if(card.charAt(1)==currentTopCard.charAt(1)){
            return true;
        }
        return false;
    }

    public boolean isRoundUnplayable(){
        for (Player player : players) {
            if (hasPlayableCard(player)) {
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

    public String[] drawUpTo3(String[] riggedCards){
        plus2Played = false;
        String[] drewCards = new String[3];

            for (int i = 0; i < 3; i++) {
                ArrayList<String> playerHand = players.get(currPlayerIndex).getHand();
                playerDrawCard(players.get(currPlayerIndex), riggedCards[i]);
                drewCards[i] = playerHand.get(playerHand.size()-1);

                if (hasPlayableCard(players.get(currPlayerIndex))) {
                    playCard(playerHand.get(playerHand.size()-1));
                    turnFinished();
                    return drewCards;
                }
            }
        plus2Played = true;
        turnFinished();
        return drewCards;
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

    public String showCardAndTurn(){
        return "Current Card: " + currentTopCard + "\n" + getCurrentPlayerTurn()+ "'s turn";
    }

    public String repromptForCard(boolean canPlayResponse){
        String prompt = "";
        if (!canPlayResponse) {
            prompt = "The card you selected cannot be played, input another card";
        }
        return prompt;
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

    public int getCurrentFirstPlayer() {
        return currentFirstPlayer;
    }

    public int getPlus2Stack() {
        return plus2Stack;
    }
}

