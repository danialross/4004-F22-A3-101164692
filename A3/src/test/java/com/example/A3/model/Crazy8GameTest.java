package com.example.A3.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        assertNotNull(game.drawCard(null));
        assertEquals("AC",game.drawCard("AC"));
        assertNull(game.drawCard("AX"));
        assertFalse(cards.contains("AC"));
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
        for (Player player : players) {
            assertEquals(5, player.getHand().size());
        }
        assertEquals(32,game.getDeck().size());
    }

    @Test
    void playCard() {

        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("5S")));
        game.playCard("5S");
        assertEquals("5S",game.getCurrentTopCard());
        assertEquals(1,game.getCurrPlayerIndex());
        assertFalse(game.getPlayers().get(0).getHand().contains("5S"));

        game = new Crazy8Game(players);
        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("AD")));
        game.getPlayers().get(3).setHand(new ArrayList<>(List.of("AS")));
        game.playCard("AD");
        assertEquals(-1,game.getDirection());
        assertFalse(game.getPlayers().get(0).getHand().contains("AD"));

        game.playCard("AS");
        assertEquals(1,game.getDirection());
        assertFalse(game.getPlayers().get(0).getHand().contains("AS"));

        //p1-p3
        game = new Crazy8Game(players);
        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("QD")));
        game.playCard("QD");
        assertFalse(game.getPlayers().get(0).getHand().contains("QD"));
        assertEquals("p3",game.getCurrentPlayerTurn());


        //p3-p1
        game = new Crazy8Game(players);
        game.getPlayers().get(2).setHand(new ArrayList<>(List.of("AD")));
        game.setCurrPlayerIndex(2);
        game.playCard("QC");
        assertEquals("p1",game.getCurrentPlayerTurn());
        assertFalse(game.getPlayers().get(0).getHand().contains("AD"));

        //p2-p4
        game = new Crazy8Game(players);
        game.getPlayers().get(1).setHand(new ArrayList<>(List.of("AD")));
        game.setDirection(-1);
        game.setCurrPlayerIndex(1);
        game.playCard("QH");
        assertEquals("p4",game.getCurrentPlayerTurn());
        assertFalse(game.getPlayers().get(0).getHand().contains("AD"));

        //p4-p2
        game = new Crazy8Game(players);
        game.getPlayers().get(1).setHand(new ArrayList<>(List.of("AD")));
        game.setDirection(-1);
        game.setCurrPlayerIndex(1);
        game.playCard("QS");
        assertEquals("p4",game.getCurrentPlayerTurn());
        assertFalse(game.getPlayers().get(0).getHand().contains("AD"));

    }

    @Test
    void turnFinished() {

        game.turnFinished();
        assertEquals(players.get(1).getName(),game.getCurrentPlayerTurn());

        game.turnFinished();
        game.turnFinished();
        game.turnFinished();
        assertEquals(players.get(0).getName(),game.getCurrentPlayerTurn());

        game.setDirection(-1);
        game.turnFinished();
        assertEquals(players.get(3).getName(),game.getCurrentPlayerTurn());


    }

    @Test
    void showDirection() {
        assertEquals("Left",game.showDirection());
        game.setDirection(-1);
        assertEquals("Right",game.showDirection());
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
        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("7S","QC")));
        game.setCurrentTopCard("7H");
        assertTrue(game.hasPlayableCard(game.getPlayers().get(0)));
        game.setCurrentTopCard("2C");
        assertTrue(game.hasPlayableCard(game.getPlayers().get(0)));
        game.getPlayers().get(0).setHand(new ArrayList<>(Arrays.asList("8S","QC")));
        game.setCurrentTopCard("3D");
        assertTrue(game.hasPlayableCard(game.getPlayers().get(0)));
        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("QC")));
        game.setCurrentTopCard("3D");
        assertFalse(game.hasPlayableCard(game.getPlayers().get(0)));

        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("QC")));
        game.setPlus2Played(true);
        game.setCurrentTopCard("2C");
        assertFalse(game.hasPlayableCard(game.getPlayers().get(0)));

        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("2C")));
        game.setPlus2Played(true);
        game.setCurrentTopCard("2D");
        assertTrue(game.hasPlayableCard(game.getPlayers().get(0)));

    }

    @Test
    void calculateHand() {
        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("AD")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(1,game.getPlayers().get(0).getScore());

        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("2D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(3,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(3,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("4D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(4,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("5D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(5,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("6D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(6,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("7D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(7,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("8D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(50,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("9D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(9,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("10D")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(10,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("JD")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(10,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("QD")));
        game.calculateHand(game.getPlayers().get(0));
        assertEquals(10,game.getPlayers().get(0).getScore());
        game.getPlayers().get(0).setScore(0);

        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("KD")));
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
        assertNotNull(game.getCurrentTopCard());
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

    @Test
    void getWinner() {
        game.getPlayers().get(0).setScore(102);
        game.getPlayers().get(1).setScore(1);
        game.getPlayers().get(2).setScore(23);
        game.getPlayers().get(3).setScore(46);
        assertEquals(game.getPlayers().get(1),game.getWinner());
        game.getPlayers().get(0).setScore(1);
        game.getPlayers().get(1).setScore(102);
        game.getPlayers().get(2).setScore(1);
        assertEquals(game.getPlayers().get(0),game.getWinner());

    }

    @Test
    void isRoundUnplayable() {
        //able to draw a card
        game.setCurrentTopCard("9C");
        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3S")));
        game.getPlayers().get(1).setHand(new ArrayList<>(List.of("4D")));
        game.getPlayers().get(2).setHand(new ArrayList<>(List.of("7H")));
        game.getPlayers().get(3).setHand(new ArrayList<>(List.of("3S")));
        assertFalse(game.isRoundUnplayable());

        //p2 able to play card match suit
        game.setDeck(new ArrayList<>());
        game.setCurrentTopCard("9D");
        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3S")));
        game.getPlayers().get(1).setHand(new ArrayList<>(List.of("4D")));
        game.getPlayers().get(2).setHand(new ArrayList<>(List.of("7H")));
        game.getPlayers().get(3).setHand(new ArrayList<>(List.of("3S")));
        assertFalse(game.isRoundUnplayable());

        //p4 able to play card crazy 8
        game.setCurrentTopCard("AD");
        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3S")));
        game.getPlayers().get(1).setHand(new ArrayList<>(List.of("4C")));
        game.getPlayers().get(2).setHand(new ArrayList<>(List.of("7H")));
        game.getPlayers().get(3).setHand(new ArrayList<>(List.of("8S")));
        assertFalse(game.isRoundUnplayable());

        //p1 able to play card match rank
        game.setCurrentTopCard("AD");
        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("AS")));
        game.getPlayers().get(1).setHand(new ArrayList<>(List.of("4C")));
        game.getPlayers().get(2).setHand(new ArrayList<>(List.of("7H")));
        game.getPlayers().get(3).setHand(new ArrayList<>(List.of("8S")));
        assertFalse(game.isRoundUnplayable());

        game.setCurrentTopCard("JD");
        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3S")));
        game.getPlayers().get(1).setHand(new ArrayList<>(List.of("4C")));
        game.getPlayers().get(2).setHand(new ArrayList<>(List.of("7H")));
        game.getPlayers().get(3).setHand(new ArrayList<>(List.of("3S")));
        assertTrue(game.isRoundUnplayable());
    }

    @Test
    void didFinishRound() {
        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3S,7H")));
        game.getPlayers().get(1).setHand(new ArrayList<>());
        game.getPlayers().get(2).setHand(new ArrayList<>(List.of("7H,3C,9S")));
        game.getPlayers().get(3).setHand(new ArrayList<>(List.of("3H")));
        assertTrue(game.didFinishRound());

        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3S,7H")));
        game.getPlayers().get(1).setHand(new ArrayList<>(List.of("AS")));
        game.getPlayers().get(2).setHand(new ArrayList<>(List.of("7H,3C,9S")));
        game.getPlayers().get(3).setHand(new ArrayList<>(List.of("3H")));
        assertFalse(game.didFinishRound());

    }


    @Test
    void showScores() {
        String expectedString = """
                --Player scores--
                p1 : 0 Points
                p2 : 0 Points
                p3 : 0 Points
                p4 : 0 Points
                """;
        System.out.print(expectedString);
        assertEquals(expectedString,game.showScores());

    }

    @Test
    void respondWith2Card() {
        game.playCard("2S");
        game.respondWith2Card(true,null);
        assertTrue(game.isPlus2Played());
        assertEquals(0,game.getPlayers().get(0).getHand().size());
        assertEquals(1,game.getCurrPlayerIndex());
        game.turnFinished();

        game.respondWith2Card(false,null);
        game.turnFinished();
        assertEquals(2,game.getPlayers().get(2).getHand().size());
        assertFalse(game.isPlus2Played());
        assertEquals(3,game.getCurrPlayerIndex());


    }

    @Test
    void drawUpTo3() {
        game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3S","6D")));
        game.setCurrentTopCard("5H");
        game.drawUpTo3(new String[]{"KS","7D","5S"});
        assertTrue(game.getPlayers().get(0).getHand().equals(new ArrayList<>(List.of("3S","6D","KS","7D"))));

        game.getPlayers().get(1).setHand(new ArrayList<>(List.of("3H","6D")));
        game.drawUpTo3(new String[]{"QH","9D","9H","AH"});
        assertTrue(game.getCurrentTopCard()=="5S");
        assertTrue(game.getPlayers().get(1).getHand().equals(new ArrayList<>(List.of("3H","6D","QH","9D","9H"))));

    }
}