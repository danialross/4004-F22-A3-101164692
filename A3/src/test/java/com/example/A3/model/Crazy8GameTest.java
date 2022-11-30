package com.example.A3.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class Crazy8GameTest {

    ArrayList<Player> players = new ArrayList<>(Arrays.asList(
            new Player(null,"p1"),
            new Player(null,"p2"),
            new Player(null,"p3"),
            new Player(null,"p4")));
    Crazy8Game game = new Crazy8Game(players);

    @Test
    void generateDeck() {
        ArrayList<String> cards = game.getDeck();
        String[] deck = {
                "AD", "AC", "AH", "AS",
                "2D", "2C", "2H", "2S",
                "3D", "3C", "3H", "3S",
                "4D", "4C", "4H", "4S",
                "5D", "5C", "5H", "5S",
                "6D", "6C", "6H", "6S",
                "7D", "7C", "7H", "7S",
                "8D", "8C", "8H", "8S",
                "9D", "9C", "9H", "9S",
                "10D", "10C", "10H", "10S",
                "JD", "JC", "JH", "JS",
                "QD", "QC", "QH", "QS",
                "KD", "KC", "KH", "KS",
        };

        assertEquals(52, cards.size());
        for (int i = 0; i < cards.size(); i++) {
            assertTrue(cards.contains(deck[i]));
        }
    }

    @Test
    void drawCard() {
        ArrayList<String> cards = game.getDeck();
        assertTrue(game.drawCard(null)!=null);
        assertEquals("AC",game.drawCard("AC"));
        assertEquals(null,game.drawCard("AX"));
    }

    @Test
    void playerDrawCard() {

        game.playerDrawCard(game.getPlayers().get(0),null);
        assertEquals(1,game.getPlayers().get(0).getHand().size());

        game.playerDrawCard(game.getPlayers().get(1),"KS");
        assertEquals(1,game.getPlayers().get(1).getHand().size());
        assertEquals("KS",game.getPlayers().get(1).getHand().get(0));
    }

    @Test
    void dealPlayerCards() {

        game.dealPlayerCards();
        for(int i = 0;i< players.size();i++){
            assertEquals(5,players.get(i).getHand().size());
        }
        assertEquals(32,game.getDeck().size());
    }

    @Test
    void playCard() {

        game.playCard("5S");
        assertEquals("5S",game.getCurrentTopCard());

        game = new Crazy8Game(players);
        game.playCard("AD");
        assertEquals(-1,game.getDirection());
        game = new Crazy8Game(players);
        game.playCard("AC");
        assertEquals(-1,game.getDirection());
        game = new Crazy8Game(players);
        game.playCard("AH");
        assertEquals(-1,game.getDirection());
        game = new Crazy8Game(players);
        game.playCard("AS");
        assertEquals(-1,game.getDirection());
        game = new Crazy8Game(players);
        game.setDirection(-1);
        game.playCard("AS");
        assertEquals(1,game.getDirection());

        //p1-p3
        game = new Crazy8Game(players);
        game.playCard("QD");
        assertEquals("p3",game.getCurrentPlayerTurn());
        //p3-p1
        game = new Crazy8Game(players);
        game.setCurrPlayerIndex(2);
        game.playCard("QC");
        assertEquals("p1",game.getCurrentPlayerTurn());
        //p3-p1
        game = new Crazy8Game(players);
        game.setDirection(-1);
        game.setCurrPlayerIndex(2);
        game.playCard("QH");
        assertEquals("p1",game.getCurrentPlayerTurn());
        //p4-p2
        game = new Crazy8Game(players);
        game.setDirection(-1);
        game.setCurrPlayerIndex(1);
        game.playCard("QS");
        assertEquals("p4",game.getCurrentPlayerTurn());

    }

    @Test
    void getNextPlayer() {

        game.getNextPlayer();
        assertEquals(players.get(1).getName(),game.getCurrentPlayerTurn());

        game.getNextPlayer();
        game.getNextPlayer();
        game.getNextPlayer();
        assertEquals(players.get(0).getName(),game.getCurrentPlayerTurn());

        game.setDirection(-1);
        game.getNextPlayer();
        assertEquals(players.get(3).getName(),game.getCurrentPlayerTurn());


    }

    @Test
    void showDirection() {
        assertEquals("Right",game.showDirection());
        game.setDirection(-1);
        assertEquals("Left",game.showDirection());
    }

    @Test
    void notifyAction() {

        // 1 = roundFinished
        // 2 = Ace played
        assertEquals("an Ace was played, the direction has been reversed",game.notifyAction(2));
        // 3 = Q played
        assertEquals("a Queen was played, the next player's turn has been skipped",game.notifyAction(3));
        // 4 = suit has changed
        game.setCurrentTopCard("8S");
        assertEquals("The suit has changed to " + game.getCurrentTopCard().charAt(1) + " because an 8 was played",game.notifyAction(4));

    }

    @Test
    void showLastCard() {

        ArrayList<String> hand = new ArrayList<>();
        hand.add("3H");
        game.getPlayers().get(0).setHand(hand);
        assertEquals("3H was drew",game.showLastCard(players.get(0)));

    }

    @Test
    void changeSuit() {
        game.setCurrentTopCard("8S");
        game.changeSuit("H");
        assertEquals("8H",game.getCurrentTopCard());

    }

    @Test
    void requestAction() {
        //ask for suit = played 8
        assertEquals("Select a new suit.(D/C/H/S)" ,game.requestAction(1));
        //play 2 or add 2 = played 2
        assertEquals("A 2 was played. Either play a 2 card or draw 2.(Index of a 2 card/-1)" ,game.requestAction(2));
    }

    @Test
    void hasPlayableCard() {
        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("8S","QC")));
        game.setCurrentTopCard("8H");
        assertEquals(true,game.hasPlayableCard(game.getPlayers().get(0), game.getCurrentTopCard()));
        game.setCurrentTopCard("2C");
        assertEquals(true,game.hasPlayableCard(game.getPlayers().get(0), game.getCurrentTopCard()));
        game.setCurrentTopCard("3D");
        assertEquals(false,game.hasPlayableCard(game.getPlayers().get(0), game.getCurrentTopCard()));
    }

    @Test
    void calculateHand() {
        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("AD")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(1,game.getPlayers().get(0).getScore());

        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("2D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(3,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("3D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(3,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("4D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(4,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("5D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(5,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("6D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(6,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("7D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(7,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("8D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(50,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("9D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(9,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("10D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(10,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("JD")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(10,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("QD")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(10,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("KD")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(10,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("AD","2D","3D","4D","5D","6D","7D","8D","9D","10D","JD","QD","KD")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(127,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);
    }

    @Test
    void dealerDrawCard() {
        game.dealerDrawCard(null);
        assertTrue(game.getCurrentTopCard()!=null);
        game.dealerDrawCard("7D");
        assertEquals("7D",game.getCurrentTopCard());

    }

    @Test
    void isDrawDeckEmpty() {
        assertFalse(game.isDrawDeckEmpty());
        game.setDeck(new ArrayList<>());
        assertTrue(game.isDrawDeckEmpty());

    }

    @Test
    void didReachWinningThreshold() {
        assertFalse(game.didReachWinningThreshold());
        game.getPlayers().get(0).setScore(99);
        assertFalse(game.didReachWinningThreshold());
        game.getPlayers().get(0).setScore(100);
        assertTrue(game.didReachWinningThreshold());
        game.getPlayers().get(0).setScore(101);
        assertTrue(game.didReachWinningThreshold());

    }
}