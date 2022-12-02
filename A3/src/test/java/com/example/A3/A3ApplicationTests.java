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
		assertThat(game.getCurrPlayerIndex(), is(1));

	}

	@Test
	void row42() {
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("AH")));
		game.getPlayers().get(3).setHand(new ArrayList<>(List.of("7H")));

		game.playCard("AH");
		assertThat(game.notifyAction(2),is("an Ace was played, the direction has been reversed"));
		assertThat(game.showDirection(),is("Current Direction is Right"));
		assertThat(this.game.getCurrPlayerIndex(), is(3));

		game.playCard("7H");
		assertThat(game.getCurrPlayerIndex(),is(2));

	}

	@Test
	void row44(){
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("QC")));

		game.playCard("QC");
		assertThat(game.notifyAction(3),is("a Queen was played, the next player's turn has been skipped"));
		assertThat(this.game.getCurrPlayerIndex(), is(2));

	}

	@Test
	void row45(){
		//setting p4 as current player
		game.setCurrPlayerIndex(3);
		game.getPlayers().get(3).setHand(new ArrayList<>(List.of("3C")));

		game.playCard("3C");
		assertThat(this.game.getCurrPlayerIndex(), is(0));

	}

	@Test
	void row46(){
		//setting p4 as current player
		game.setCurrPlayerIndex(3);
		game.getPlayers().get(3).setHand(new ArrayList<>(List.of("AH")));
		game.getPlayers().get(2).setHand(new ArrayList<>(List.of("7H")));

		game.playCard("AC");
		assertThat(this.game.getCurrPlayerIndex(), is(2));
		assertThat(game.notifyAction(2),is("an Ace was played, the direction has been reversed"));
		assertThat(game.showDirection(),is("Current Direction is Right"));

		game.playCard("7H");
		assertThat(this.game.getCurrPlayerIndex(), is(1));

	}

	@Test
	void row48(){
		//setting p4 as current player
		game.setCurrPlayerIndex(3);
		game.getPlayers().get(3).setHand(new ArrayList<>(List.of("QC")));

		game.playCard("QC");
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

		//controller will say if 2 or 8 card plays a response from the user will be asked
		assertThat(game.requestAction(1),is("Select a new suit.(D/C/H/S)"));
		assertThat(game.getCurrentTopCard(),is("8H"));
		assertThat(game.getPlayers().get(0).getHand(), Matchers.empty());
	}

	@Test
	void row54() {
		//if chat controller get empty string from repromptForCard it will playCard()
		game.getPlayers().get(0).setHand(new ArrayList<>(List.of("5S")));
		game.setCurrentTopCard("KC");

		assertThat(game.canPlay("5S"), is(false));
		assertThat(game.repromptForCard(game.canPlay("5S")), is("The card you selected cannot be played, input another card"));

		assertThat(game.getCurrentTopCard(), is("KC"));
		assertThat(game.getPlayers().get(0).getHand().get(0), is("5S"));

	}
}
