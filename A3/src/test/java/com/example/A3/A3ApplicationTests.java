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
	Crazy8Game game = new Crazy8Game(players);;
	@Test
	void row41() {
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3C")));

		game.playCard("3C");
		game.turnFinished();
		assertThat(game.getCurrPlayerIndex(), is(1));

	}

	@Test
	void row42() {
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("AH")));
		game.getPlayers().get(3).setHand(new ArrayList<>(List.of("7H")));

		game.playCard("AH");
		game.turnFinished();
		assertThat(game.notifyAction(2),is("an Ace was played, the direction has been reversed"));
		assertThat(game.showDirection(),is("Current Direction is Right"));
		assertThat(this.game.getCurrPlayerIndex(), is(3));

		game.playCard("7H");
		game.turnFinished();
		assertThat(game.getCurrPlayerIndex(),is(2));

	}

	@Test
	void row44(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("QC")));

		game.playCard("QC");
		game.turnFinished();
		assertThat(game.notifyAction(3),is("a Queen was played, the next player's turn has been skipped"));
		assertThat(this.game.getCurrPlayerIndex(), is(2));

	}

	@Test
	void row45(){
		//setting p4 as current player
		game.setCurrPlayerIndex(3);
		game.getPlayers().get(3).setHand(new ArrayList<>(List.of("3C")));

		game.playCard("3C");
		game.turnFinished();
		assertThat(this.game.getCurrPlayerIndex(), is(0));

	}

	@Test
	void row46(){
		//setting p4 as current player
		game.setCurrPlayerIndex(3);
		game.getPlayers().get(3).setHand(new ArrayList<>(List.of("AH")));
		game.getPlayers().get(2).setHand(new ArrayList<>(List.of("7H")));

		game.playCard("AC");
		game.turnFinished();
		assertThat(this.game.getCurrPlayerIndex(), is(2));
		assertThat(game.notifyAction(2),is("an Ace was played, the direction has been reversed"));
		assertThat(game.showDirection(),is("Current Direction is Right"));

		game.playCard("7H");
		game.turnFinished();
		assertThat(this.game.getCurrPlayerIndex(), is(1));

	}

	@Test
	void row48(){
		//setting p4 as current player
		game.setCurrPlayerIndex(3);
		game.getPlayers().get(3).setHand(new ArrayList<>(List.of("QC")));

		game.playCard("QC");
		game.turnFinished();
		assertThat(game.notifyAction(3),is("a Queen was played, the next player's turn has been skipped"));
		assertThat(this.game.getCurrPlayerIndex(), is(1));

	}

	@Test
	void row51(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("KH")));
		game.setCurrentTopCard("KC");

		assertThat(game.canPlay("KH"),is(true));
		assertThat(game.repromptForCard(game.canPlay("KH")),is(""));
		game.playCard("KH");
		game.turnFinished();
		assertThat(game.getCurrentTopCard(),is("KH"));
		assertThat(game.getPlayers().get(0).getHand(), Matchers.empty());

	}

	@Test
	void row52(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("7C")));
		game.setCurrentTopCard("KC");

		assertThat(game.canPlay("7C"),is(true));
		assertThat(game.repromptForCard(game.canPlay("7C")),is(""));
		game.playCard("7C");
		game.turnFinished();
		assertThat(game.getCurrentTopCard(),is("7C"));
		assertThat(game.getPlayers().get(0).getHand(), Matchers.empty());
	}
	@Test
	void row53(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("8H")));
		game.setCurrentTopCard("KC");

		assertThat(game.canPlay("8H"),is(true));
		assertThat(game.repromptForCard(game.canPlay("8H")),is(""));
		game.playCard("8H");
		game.turnFinished();

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

		assertThat(game.canPlay("5S"),is(false));
		assertThat(game.repromptForCard(game.canPlay("5S")),is("The card you selected cannot be played, input another card"));

		assertThat(game.getCurrentTopCard(),is("KC"));
		assertThat(game.getPlayers().get(0).getHand().get(0),is("5S"));

	}

	@Test
	void row58(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3H")));
		game.setCurrentTopCard("7C");
		game.drawUpTo3("6C");
		game.playCard("6C");
		game.turnFinished();

		ArrayList<String> expected = new ArrayList<>(List.of("3H"));
		assertEquals(expected,game.getPlayers().get(0).getHand());
		assertThat(game.getCurrentTopCard(),is("6C"));
	}

	@Test
	void row59(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3H")));
		game.setCurrentTopCard("7C");
		game.drawUpTo3("6D");
		game.drawUpTo3("5C");
		game.playCard("5C");
		game.turnFinished();

		ArrayList<String> expected = new ArrayList<>(List.of("3H","6D"));
		assertEquals(expected,game.getPlayers().get(0).getHand());
		assertThat(game.getCurrentTopCard(),is("5C"));

	}

	@Test
	void row60(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3H")));
		game.setCurrentTopCard("7C");
		game.drawUpTo3("6D");
		game.drawUpTo3("5S");
		game.drawUpTo3("7H");
		game.playCard("7H");
		game.turnFinished();

		ArrayList<String> expected = new ArrayList<>(List.of("3H","6D","5S"));
		assertEquals(expected,game.getPlayers().get(0).getHand());
		assertThat(game.getCurrentTopCard(),is("7H"));

	}

	@Test
	void row61(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3H")));
		game.setCurrentTopCard("7C");
		game.drawUpTo3("6D");
		game.drawUpTo3("5S");
		game.drawUpTo3("4H");

		ArrayList<String> expected = new ArrayList<>(List.of("3H","6D","5S","4H"));
		assertEquals(expected,game.getPlayers().get(0).getHand());
		assertThat(game.getCurrentTopCard(),is("7C"));

	}
	@Test
	void row62(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("3H")));
		game.setCurrentTopCard("7C");
		game.drawUpTo3("6D");
		game.drawUpTo3("5S");
		game.drawUpTo3("8H");
		game.playCard("8H");
		game.turnFinished();

		ArrayList<String> expected = new ArrayList<>(List.of("3H","6D","5S"));
		assertEquals(expected,game.getPlayers().get(0).getHand());
		assertThat(game.getCurrentTopCard(),is("8H"));
	}

	@Test
	void row63(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("KS","3C")));
		game.setCurrentTopCard("7C");
		game.drawUpTo3("6C");
		game.playCard("6C");
		game.turnFinished();

		ArrayList<String> expected = new ArrayList<>(List.of("KS","3C"));
		assertEquals(expected,game.getPlayers().get(0).getHand());
		assertThat(game.getCurrentTopCard(),is("6C"));
	}

}
