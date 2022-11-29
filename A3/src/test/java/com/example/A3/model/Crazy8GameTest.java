package com.example.A3.model;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class Crazy8GameTest {
    Crazy8Game game;
    ArrayList<Player> players = new ArrayList<>();

    @Test
    void generateDeck() {
        game = new Crazy8Game(null);
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
        game = new Crazy8Game(null);
        ArrayList<String> cards = game.getDeck();

        assertEquals("AC",game.drawCard("AC"));
        assertEquals(null,game.drawCard("AX"));
    }

    @Test
    void playerDrawCard() {
        players.add(new Player(null,"p1"));
        players.add(new Player(null,"p2"));
        players.add(new Player(null,"p3"));
        players.add(new Player(null,"p4"));
        game = new Crazy8Game(players);

        game.playerDrawCard(game.getPlayers().get(0),null);
        assertEquals(1,game.getPlayers().get(0).getHand().size());

        game.playerDrawCard(game.getPlayers().get(1),"KS");
        assertEquals(1,game.getPlayers().get(1).getHand().size());
        assertEquals("KS",game.getPlayers().get(1).getHand().get(0));
    }

    @Test
    void dealPlayerCards() {

        players.add(new Player(null,"p1"));
        players.add(new Player(null,"p2"));
        players.add(new Player(null,"p3"));
        players.add(new Player(null,"p4"));
        game = new Crazy8Game(players);
        game.dealPlayerCards();
        for(int i = 0;i< players.size();i++){
            assertEquals(5,players.get(i).getHand().size());
        }
        assertEquals(32,game.getDeck().size());
    }

    @Test
    void playCard() {
        players.add(new Player(null,"p1"));
        players.add(new Player(null,"p2"));
        players.add(new Player(null,"p3"));
        players.add(new Player(null,"p4"));
        game = new Crazy8Game(players);
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
        players.add(new Player(null,"p1"));
        players.add(new Player(null,"p2"));
        players.add(new Player(null,"p3"));
        players.add(new Player(null,"p4"));
        game = new Crazy8Game(players);
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
}