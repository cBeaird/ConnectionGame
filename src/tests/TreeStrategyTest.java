package tests;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import connect4.Connect4Board;
import connect4.Connect4Game;
import connect4.GamePieces;
import connect4.RandomStrategy;
import connect4.SmarterRandomStrategy;
import connect4.TreeSearchStrategy;
import connectionAPI.GameBoard;
import connectionAPI.Player;
import connectionAPI.Strategy;

public class TreeStrategyTest {
	public static void main(String[] args) {


		Strategy me = new TreeSearchStrategy(4);

		List<Strategy> otherStrategies = new ArrayList<>();
		otherStrategies.add(new RandomStrategy());
		otherStrategies.add(new SmarterRandomStrategy());
		otherStrategies.add(new TreeSearchStrategy(2));
		otherStrategies.add(new TreeSearchStrategy(4));
		otherStrategies.add(new TreeSearchStrategy(6));
		//otherStrategies.add(new MCTS_UCTStrategy(100));
		for (Strategy other : otherStrategies) {
			int winCount = 0;
			int loseCount = 0;
			int drawCount = 0;
			for (int i = 0; i < 100; i++) {
				Connect4Game c4g;
				ArrayList<Player> players = new ArrayList<>();

				for (Player p : GamePieces.values()) {
					if (p == GamePieces.EMPTY)
						continue;
					if (p.turn() == 1)
						p.setPlayerStrategy(other);
					if (p.turn() == 0)
						p.setPlayerStrategy(me);
					players.add(p);
				}
				// build a new game with players
				c4g = new Connect4Game(5, 6, players);

				((Connect4Board) c4g.getGameBoard()).setConnectionLength(4);
				c4g.playSilent();
				GameBoard board = (GameBoard) c4g.getGameBoard();

				if (board.getWinner() == GamePieces.WHITE) {
					winCount++;
				} else if (board.getWinner() == GamePieces.BLACK) {
					loseCount++;
				} else {
					drawCount++;
				}
			}

			int total = winCount + loseCount + drawCount;
			NumberFormat df = NumberFormat.getPercentInstance();

			System.out.println(me.getStrategyName() + " vs " + other.getStrategyName() + " (opponent)");
			System.out.println("Wins: " + df.format((double) winCount / (double) total) + "("+winCount +"/"+total+")");
			System.out.println("Loses: " + df.format((double) loseCount / (double) total) + "("+loseCount +"/"+total+")");
			System.out.println("Draws: " + df.format((double) drawCount / (double) total) + "("+drawCount +"/"+total+")");
			System.out.println();
		}
	}

}
