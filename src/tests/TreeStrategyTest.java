package tests;

import java.util.ArrayList;

import connect4.Connect4Board;
import connect4.Connect4Game;
import connect4.GamePieces;
import connect4.InteractiveStrategy;
import connect4.TreeSearchStrategy;
import connectionAPI.Player;

public class TreeStrategyTest {
	public static void main(String[] args) {
		Connect4Game c4g;
		ArrayList<Player> players = new ArrayList<>();

		for (Player p : GamePieces.values()) {
			if (p == GamePieces.EMPTY)
				continue;
			if (p.turn() == 1)
				p.setPlayerStrategy(new InteractiveStrategy());
			if (p.turn() == 0)
				p.setPlayerStrategy(new TreeSearchStrategy(2));
			players.add(p);
		}

		// build a new game with players
		c4g = new Connect4Game(3, 4, players);

		((Connect4Board) c4g.getGameBoard()).setBoardSpace(0, 0, GamePieces.WHITE);
		((Connect4Board) c4g.getGameBoard()).setBoardSpace(0, 1, GamePieces.BLACK);
		((Connect4Board) c4g.getGameBoard()).setBoardSpace(1, 0, GamePieces.WHITE);
		((Connect4Board) c4g.getGameBoard()).setBoardSpace(2, 0, GamePieces.BLACK);
		((Connect4Board) c4g.getGameBoard()).setConnectionLength(3);
		c4g.play();
		System.out.println(((Connect4Board) c4g.getGameBoard()).getWinner());
		System.exit(0);
	}
}
