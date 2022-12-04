package com.example.A3;

import com.example.A3.model.Crazy8Game;
import com.example.A3.model.Player;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class A3ApplicationTests {


	ArrayList<Player> players = new ArrayList<>(List.of(
			new Player(null,"p1"),
			new Player(null,"p2"),
			new Player(null,"p3"),
			new Player(null,"p4")));
	Crazy8Game game = new Crazy8Game(players);
	@Test
	void row41() {
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3C")));
		game.setCurrentTopCard("4C");

		game.playRound("3C",null,null,null,null);
		assertThat(game.getCurrPlayerIndex(), is(1));

	}

	@Test
	void row42() {
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("AH")));
		game.getPlayers().get(3).setHand(new ArrayList<>(List.of("7H")));
		game.setCurrentTopCard("4H");

		game.playRound("AH",null,null,null,null);
		assertThat(game.notifyAction(2),is("an Ace was played, the direction has been reversed"));
		assertThat(game.showDirection(),is("Current Direction is Right"));
		assertThat(this.game.getCurrPlayerIndex(), is(3));

		game.playRound("7H",null,null,null,null);
		assertThat(game.getCurrPlayerIndex(),is(2));

	}

	@Test
	void row44(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("QC")));
		game.setCurrentTopCard("4C");

		game.playRound("QC",null,null,null,null);
		assertThat(game.notifyAction(3),is("a Queen was played, the next player's turn has been skipped"));
		assertThat(this.game.getCurrPlayerIndex(), is(2));

	}

	@Test
	void row45(){
		//setting p4 as current player
		game.setCurrPlayerIndex(3);
		game.getPlayers().get(3).setHand(new ArrayList<>(List.of("3C")));
		game.setCurrentTopCard("4C");

		game.playRound("3C",null,null,null,null);
		assertThat(this.game.getCurrPlayerIndex(), is(0));

	}

	@Test
	void row46(){
		//setting p4 as current player
		game.setCurrPlayerIndex(3);
		game.getPlayers().get(3).setHand(new ArrayList<>(List.of("AC")));
		game.getPlayers().get(2).setHand(new ArrayList<>(List.of("7H")));
		game.setCurrentTopCard("4C");

		game.playRound("AC",null,null,null,null);
		assertThat(this.game.getCurrPlayerIndex(), is(2));
		assertThat(game.notifyAction(2),is("an Ace was played, the direction has been reversed"));
		assertThat(game.showDirection(),is("Current Direction is Right"));

		game.playRound("7H",null,null,null,null);
		assertThat(this.game.getCurrPlayerIndex(), is(2));

	}

	@Test
	void row48(){
		//setting p4 as current player
		game.setCurrPlayerIndex(3);
		game.getPlayers().get(3).setHand(new ArrayList<>(List.of("QC")));
		game.setCurrentTopCard("4C");

		game.playRound("QC",null,null,null,null);
		assertThat(game.notifyAction(3),is("a Queen was played, the next player's turn has been skipped"));
		assertThat(this.game.getCurrPlayerIndex(), is(1));

	}

	@Test
	void row51(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("KH")));
		game.setCurrentTopCard("KC");
		game.setCurrentTopCard("4H");

		assertThat(game.canPlay("KH"),is(true));
		assertThat(game.repromptForCard(game.canPlay("KH")),is(""));
		game.playRound("KH",null,null,null,null);
		assertThat(game.getCurrentTopCard(),is("KH"));
		assertThat(game.getPlayers().get(0).getHand(), Matchers.empty());

	}

	@Test
	void row52(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("7C")));
		game.setCurrentTopCard("KC");

		assertThat(game.canPlay("7C"),is(true));
		assertThat(game.repromptForCard(game.canPlay("7C")),is(""));
		game.playRound("7C",null,null,null,null);
		assertThat(game.getCurrentTopCard(),is("7C"));
		assertThat(game.getPlayers().get(0).getHand(), Matchers.empty());
	}
	@Test
	void row53(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("8H")));
		game.setCurrentTopCard("KH");

		assertThat(game.canPlay("8H"),is(true));
		assertThat(game.repromptForCard(game.canPlay("8H")),is(""));
		game.playRound("8H",null,null,null,null);

		//controller will say if 2 or 8 card plays a response from the user will be asked
		assertThat(game.requestAction(1),is("Select a new suit.(D/C/H/S)"));
		assertThat(game.getCurrentTopCard(),is("8H"));
		assertThat(game.getPlayers().get(0).getHand(), Matchers.empty());
	}

	@Test
	void row54(){
		//if chat controller get empty string from repromptForCard it will playCard()
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("5S")));
		game.setCurrentTopCard("KC");

		assertThat(game.repromptForCard(game.playRound("5S",null,null,null,null)), is("The card you selected cannot be played, input another card"));

		assertThat(game.getCurrentTopCard(),is("KC"));
		assertThat(game.getPlayers().get(0).getHand().get(0),is("5S"));

	}

	@Test
	void row58(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3H")));
		game.setCurrentTopCard("7C");

		game.playRound(null,null,null,new String[]{"6C"},null);
		ArrayList<String> expected = new ArrayList<>(List.of("3H"));
		assertEquals(expected,game.getPlayers().get(0).getHand());
		assertThat(game.getCurrentTopCard(),is("6C"));
	}

	@Test
	void row59(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3H")));
		game.setCurrentTopCard("7C");

		game.playRound(null,null,null,new String[]{"6D","5C"},null);
		ArrayList<String> expected = new ArrayList<>(List.of("3H","6D"));
		assertEquals(expected,game.getPlayers().get(0).getHand());
		assertThat(game.getCurrentTopCard(),is("5C"));

	}

	@Test
	void row60(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3H")));
		game.setCurrentTopCard("7C");

		game.playRound(null,null,null,new String[]{"6D","5S","7H"},null);
		ArrayList<String> expected = new ArrayList<>(List.of("3H","6D","5S"));
		assertEquals(expected,game.getPlayers().get(0).getHand());
		assertThat(game.getCurrentTopCard(),is("7H"));

	}

	@Test
	void row61(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3H")));
		game.setCurrentTopCard("7C");

		game.playRound(null,null,null,new String[]{"6D","5S","4H"},null);

		ArrayList<String> expected = new ArrayList<>(List.of("3H","6D","5S","4H"));
		assertEquals(expected,game.getPlayers().get(0).getHand());
		assertThat(game.getCurrentTopCard(),is("7C"));

	}
	@Test
	void row62(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3H")));
		game.setCurrentTopCard("7C");

		game.playRound(null,null,null,new String[]{"6D","5S","8H"},null);
		ArrayList<String> expected = new ArrayList<>(List.of("3H","6D","5S"));
		assertEquals(expected,game.getPlayers().get(0).getHand());
		assertThat(game.getCurrentTopCard(),is("8H"));
	}

	@Test
	void row63(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("KS","3C")));
		game.setCurrentTopCard("7C");

		game.playRound(null,null,null,new String[]{"6C"},null);
		ArrayList<String> expected = new ArrayList<>(List.of("KS","3C"));
		assertEquals(expected,game.getPlayers().get(0).getHand());
		assertThat(game.getCurrentTopCard(),is("6C"));
	}
	//p1 plays 2C, p2 has only  {4H} thus must draw 2 cards {6C and 9D} then plays 6C
	@Test
	void row67(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("2C")));
		game.getPlayers().get(1).setHand(new ArrayList<>(List.of("4H")));
		game.setCurrentTopCard("3C");

		game.playRound("2C",null,null,null,null);

		assertThat(game.hasPlayableCard(game.getPlayers().get(game.getCurrPlayerIndex())),is(false));
		game.playRound(null,new String[]{"6C","9D"},null,null,"6C");

		ArrayList<String> expected = new ArrayList<>(List.of("4H","9D"));
		assertEquals(expected,game.getPlayers().get(1).getHand());

	}

	//p1 plays 2C, p2 has only {4H}, draws {6S and 9D}, still can't play, then draws 9H then 6C and then must play 6C
	@Test
	void row68(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("2C")));
		game.getPlayers().get(1).setHand(new ArrayList<>(List.of("4H")));
		game.setCurrentTopCard("3C");

		game.playRound("2C",null,null,null,null);

		assertThat(game.hasPlayableCard(game.getPlayers().get(game.getCurrPlayerIndex())),is(false));
		game.playRound(null,new String[]{"6S","9D"},null,new String[]{"9H","6C"},null);

		ArrayList<String> expected = new ArrayList<>(List.of("4H","6S","9D","9H"));
		assertEquals(expected,game.getPlayers().get(1).getHand());


	}

	//p1 plays 2C, p2 has only {4H} draws {6S and 9D} then draws 9H, 7S, 5H and then  turns end (without playing a card)
	@Test
	void row69(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("2C")));
		game.getPlayers().get(1).setHand(new ArrayList<>(List.of("4H")));
		game.setCurrentTopCard("3C");

		game.playRound("2C",null,null,null,null);

		assertThat(game.hasPlayableCard(game.getPlayers().get(game.getCurrPlayerIndex())),is(false));
		game.playRound(null,new String[]{"6S","9D"},null,new String[]{"9H","7S","5H"},null);

		ArrayList<String> expected = new ArrayList<>(List.of("4H","6S","9D","9H","7S","5H"));
		assertEquals(expected,game.getPlayers().get(1).getHand());
		assertThat(game.getCurrPlayerIndex(),is(2));


	}

	//p1 plays 2C, p2 has {4H} draws {2H and 9D} then plays 2H (forcing next player to immediately play or draw 4 cards)
	@Test
	void row70(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("2C")));
		game.getPlayers().get(1).setHand(new ArrayList<>(List.of("4H")));
		game.getPlayers().get(2).setHand(new ArrayList<>(List.of("7D")));
		game.setCurrentTopCard("3C");

		game.playRound("2C",null,null,null,null);

		assertThat(game.hasPlayableCard(game.getPlayers().get(game.getCurrPlayerIndex())),is(false));
		game.playRound(null,new String[]{"2H","9D"},null,null,"2H");

		assertThat(game.hasPlayableCard(game.getPlayers().get(game.getCurrPlayerIndex())),is(false));

		game.playRound(null,new String[]{"5S","6D", "6H","7C"},null,null,"6H");

		assertThat(game.getCurrPlayerIndex(),is(3));


	}

	//p1 plays 2C, p2 has {4C, 6C, 9D} then p2 plays 4C and 6C (ie 2 legal cards) and ends their turn
	@Test
	void row72(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("2C")));
		game.getPlayers().get(1).setHand(new ArrayList<>(List.of("4C","6C","9D")));
		game.getPlayers().get(2).setHand(new ArrayList<>(List.of("7D")));
		game.setCurrentTopCard("3C");

		game.playRound("2C",null,null,null,null);

		assertThat(game.hasPlayableCard(game.getPlayers().get(game.getCurrPlayerIndex())),is(true));
		game.playRound(null,null,new String[]{"4C","6C"},null,null);

		ArrayList<String> expected = new ArrayList<>(List.of("9D"));
		assertEquals(expected,game.getPlayers().get(1).getHand());
		assertThat(game.getCurrPlayerIndex(),is(2));

	}


	// p1 plays 2C, p2 has {4C, 6C} then p2 plays 4C and 6C and ends round (because p2 played all their cards)
	@Test
	void row73(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("2C")));
		game.getPlayers().get(1).setHand(new ArrayList<>(List.of("4C","6C")));
		game.getPlayers().get(2).setHand(new ArrayList<>(List.of("7D")));
		game.getPlayers().get(2).setHand(new ArrayList<>(List.of("4S")));
		game.setCurrentTopCard("3C");

		game.playRound("2C",null,null,null,null);

		assertThat(game.hasPlayableCard(game.getPlayers().get(game.getCurrPlayerIndex())),is(true));
		game.playRound(null,null,new String[]{"4C","6C"},null,null);

		assertThat(game.didFinishRound(),is(true));

	}

	@Test
	void row77(){
		game.playerDrawCard(game.getPlayers().get(0),"AS");

		game.playerDrawCard(game.getPlayers().get(2),"8H");
		game.playerDrawCard(game.getPlayers().get(2),"JH");
		game.playerDrawCard(game.getPlayers().get(2),"6H");
		game.playerDrawCard(game.getPlayers().get(2),"KH");
		game.playerDrawCard(game.getPlayers().get(2),"KS");

		game.playerDrawCard(game.getPlayers().get(3),"8C");
		game.playerDrawCard(game.getPlayers().get(3),"8D");
		game.playerDrawCard(game.getPlayers().get(3),"2D");

		assertThat(game.endGame(),is(
    	"""
		Round has Ended
		--Player scores--
		p1 : 1 Points
		p2 : 0 Points
		p3 : 86 Points
		p4 : 102 Points
		Game has Ended
		p2 is the Winner!
		"""));

	}
}
