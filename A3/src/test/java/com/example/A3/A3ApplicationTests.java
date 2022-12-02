package com.example.A3;

import com.example.A3.model.Crazy8Game;
import com.example.A3.model.Player;
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
	Crazy8Game game = new Crazy8Game(players);
	@Test
	void row41() {
		//draw card as rigging instead of setting hand to have 3C
		game.drawCard("3C");
		game.playCard("3C");
		assertThat(this.game.getCurrPlayerIndex(), is(1));

	}

}
