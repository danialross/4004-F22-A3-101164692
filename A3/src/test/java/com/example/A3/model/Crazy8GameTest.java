package com.example.A3.model;

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
}