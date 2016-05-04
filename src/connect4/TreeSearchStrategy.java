package connect4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import connectionAPI.Game;
import connectionAPI.Player;
import connectionAPI.PlayerMove;
import connectionAPI.Strategy;

/**
 * @author Casey Beaird
 * @author Chris Everitt Created on 3/20/16. Virginia Commonwealth University
 *         Computer Science Department Course 612 Game Theory
 * 
 *         Implements a naive Tree Search strategy.
 * 
 */

public class TreeSearchStrategy implements Strategy {

	private Integer maxDepth;
	private long timeStart = 0L;
	private long timeStop = 0L;
	private List<Long> getMoveDurations = new ArrayList<>();

	/**
	 * 
	 * Create a new TreeSearchStrategy that will search to {@code maxDepth}.
	 * 
	 * @param maxDepth
	 *            the maximum relative depth to search, ie the number of turns
	 *            to play out. Set this to Integer.MAX-VALUE for a full tree
	 *            search.
	 */
	public TreeSearchStrategy(Integer maxDepth) {
		this.maxDepth = maxDepth;
	}

	public long getMoveDurationInMillis() {
		timeStop = System.currentTimeMillis();
		return timeStop - timeStart;
	}

	@Override
	public String getStrategyName() {
		return this.getClass().getSimpleName() + "(" + maxDepth + ")";
	}

	@Override
	public PlayerMove getNextMove(Game game) {
		long timeStart = System.currentTimeMillis();

		Random rnd = new Random();

		Tree tree = new Tree((Connect4Game) game, maxDepth);

		// for (Tree child : tree.getChildren()) {
		// System.out.println(child.getMove() + ": " + child.minimax());
		// }

		List<PlayerMove> moves = getWinningMoves(tree);
		// System.out.println("winners: " + moves);
		if (!moves.isEmpty()) {
			return moves.get(rnd.nextInt(moves.size()));
		}

		moves = getBestMoves(tree);
		// System.out.println("besters: " + moves);

		getMoveDurations.add(System.currentTimeMillis() - timeStart);
		return moves.get(rnd.nextInt(moves.size()));

	}

	/**
	 * get the moves that maximize this player's utility.
	 * 
	 * @return The moves that maximize a players utility
	 */
	private List<PlayerMove> getBestMoves(Tree tree) {
		List<PlayerMove> list = new ArrayList<>();
		Integer maxUtl = Integer.MIN_VALUE;
		for (Tree child : tree.getChildren()) {
			Connect4Game game = (Connect4Game) child.getGame();
			PlayerMove move = child.getMove();
			Connect4Board board = (Connect4Board) child.getGame().getGameBoard().copy();
			board.setBoardSpace(move.getYCoordinate(), move.getXCoordinate(), (GamePieces) tree.rootPlayer());
			if (!opponentWinsAfterMove(move, game)) {
				// System.out.println("child.minimax() = "+child.minimax()+" ==
				// maxUtl="+maxUtl);
				// System.out.println(child.minimax() == maxUtl);
				if (child.minimax() > maxUtl) {
					list.clear();
					// System.out.println("Adding " + move + " for a new maxUtil
					// of " + child.minimax());
					list.add(move);
					maxUtl = child.minimax();
				} else if (child.minimax() == maxUtl) {
					// System.out.println("Adding " + move + " for a maxUtil of
					// " + child.minimax());
					list.add(move);
				}
			}
		}
		if (tree.getChildren().isEmpty()) {
			throw new AssertionError("There are no legal moves to play.");
		}
		if (list.isEmpty()) {
			// Our opponent will win in their next turn regardless of our move,
			// so return all legal moves
			maxUtl = Integer.MIN_VALUE;
			for (Tree child : tree.getChildren()) {
				PlayerMove move = child.getMove();
				if (child.minimax() > maxUtl) {
					list.clear();
					list.add(move);
				} else if (child.minimax() == maxUtl) {
					list.add(move);
				}
			}
		}
		return list;
	}

	/**
	 * 
	 * Check this move to see if it will allow the opponent to win on their next
	 * turn.
	 * 
	 * @return true if this move will create a winning move for the opponent on
	 *         the next turn, false otherwise.
	 */
	private boolean opponentWinsAfterMove(PlayerMove move, Connect4Game game) {
		Player nextPlayer = game.getPlayers().get(0).getMover((game.queryMove() - 0) % game.getPlayers().size());
		for (PlayerMove nextMoves : game.getGameBoard().getLegalMoves().values()) {
			Connect4Board nextBoard = (Connect4Board) game.copy().getGameBoard();
			nextBoard.setBoardSpace(nextMoves.getYCoordinate(), nextMoves.getXCoordinate(), (GamePieces) nextPlayer);
			if (nextBoard.getWinner().equals(nextPlayer)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get all the winning moves.
	 * 
	 * @return the list of moves that would cause our player to win, or an empty
	 *         list if there are no winning moves.
	 */
	private List<PlayerMove> getWinningMoves(Tree tree) {
		List<PlayerMove> list = new ArrayList<>();
		for (Tree child : tree.getChildren()) {
			PlayerMove move = child.getMove();
			Connect4Board board = (Connect4Board) child.getGame().getGameBoard().copy();
			board.setBoardSpace(move.getYCoordinate(), move.getXCoordinate(), (GamePieces) tree.rootPlayer());
			if (board.getWinner().equals(tree.rootPlayer())) {
				list.add(move);
			}
		}
		return list;
	}

	public List<Long> getGetMoveDurations() {
		List<Long> copy = new ArrayList<>(getMoveDurations);
		getMoveDurations = new ArrayList<>();
		return copy;
	}

}
