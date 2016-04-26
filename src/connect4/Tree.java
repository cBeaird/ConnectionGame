package connect4;

import java.util.ArrayList;
import java.util.List;

import connectionAPI.Player;
import connectionAPI.PlayerMove;

/**
 * @author Casey Beaird
 * @author Chris Everitt Created on 3/20/16. Virginia Commonwealth University
 *         Computer Science Department Course 612 Game Theory
 * 
 *         Tree implementation for naive tree search strategy.
 * 
 */
class Tree {

	private Tree parent;
	private Connect4Game game;
	private List<Tree> children;
	private Player player;
	private PlayerMove move;
	private Integer utility;
	private Integer depth;
	private Integer maxDepth;

	public Tree(Connect4Game game) {
		this(game, (Tree) null, Integer.MAX_VALUE);
	}

	public Tree(Connect4Game game, Integer maxDepth) {
		this(game, (Tree) null, maxDepth);
	}

	public Tree(Connect4Game game, Tree parent) {
		this(game, parent, Integer.MAX_VALUE);
	}

	public Tree(Connect4Game game, Tree parent, Integer maxDepth) {
		this.game = game;
		this.parent = parent;
		this.children = new ArrayList<>();
		this.utility = 0;
		this.maxDepth = maxDepth;
		this.player = game.getPlayers().get(0).getMover((game.queryMove() - 1) % game.getPlayers().size());
		if (parent == null) {
			depth = 0;
		} else {
			this.depth = parent.getDepth() + 1;
		}
		build();
	}

	public void addChild(Tree child) {
		child.setParent(this);
		children.add(child);
	}
	
	
	private Integer runCountUtilityApproximation(Connect4Game game){
		Connect4Board board = (Connect4Board) game.getGameBoard();
		Integer runCount = 0;
		for (int k = 1; k < board.getConnectionLength() - 1; k++){
	        for (int i = 0; i < board.boardHeight(); i++) {
	            for (int j = 0; j < board.boardWidth(); j++) {
	                // Any winning connection cannot start with an empty space
	                if (board.getBoardSpace(i, j) == GamePieces.EMPTY)
	                    continue;

	                // Check for a row winner
	                if ((j + board.getConnectionLength() - 1) < board.boardWidth()) {
	                    for (int c = 1; c < board.getConnectionLength(); c++) {
	                        if (board.getBoardSpace(i, j) != board.getBoardSpace(i,j + c))
	                            break;
	                        if (c == k)
	                        	runCount++;
	                    }
	                }

	                // Check for a column winner
	                if ((i + board.getConnectionLength() - 1) < board.boardHeight()) {
	                    for (int c = 1; c < board.getConnectionLength(); c++) {
	                        if (board.getBoardSpace(i, j) != board.getBoardSpace(i + c,j))
	                            break;
	                        if (c == k)
	                        	runCount++;
	                    }
	                }

	                // Check for the up right vector winner
	                if ((i + board.getConnectionLength() - 1 < board.boardHeight()) && (j + board.getConnectionLength() - 1 < board.boardWidth())) {
	                    for (int ur = 1; ur < board.getConnectionLength(); ur++) {
	                        if (board.getBoardSpace(i, j) != board.getBoardSpace(i + ur,j + ur))
	                            break;
	                       if (ur == k)
	                        	runCount++;
	                    }
	                }

	                // Check for the up left vector winner
	                if ((i + (board.getConnectionLength() - 1) < board.boardHeight()) && (j - (board.getConnectionLength() - 1)) >= 0) {
	                    for (int ul = 1; ul < board.getConnectionLength(); ul++) {
	                        if (board.getBoardSpace(i, j) != board.getBoardSpace(i + ul, j + (-1 * ul)))
	                            break;
	                      if (ul == k)
	                        	runCount++;
	                    }
	                }
	            }
	        }
		}
        return  runCount;
	}
	
	
	public Integer approximatedUtility() {
		UtilityHeuristic hueristic =  new UtilityHeuristic(game);
		Player thisPlayer = rootPlayer();
		Player currentPlayer = game.getPlayers().get(0).getMover((game.queryMove() - 1) % game.getPlayers().size());
		Integer utl = hueristic.getHueristicUtility();
		if (thisPlayer != currentPlayer) {
			utl *= -1;
		}
		return utl;
	}

	private void build() {
		Connect4Board board = (Connect4Board) game.getGameBoard();
		if (getDepth() == maxDepth) {
			setUtility(approximatedUtility());
			return;
		}

		if (board.isWon()) {
			if (board.getWinner() == rootPlayer()) {
				setUtility(1);
			} else {
				setUtility(-1);
			}
			return;
		}
		if (board.isDraw()) {
			setUtility(0);
			return;
		}

		List<PlayerMove> moves = new ArrayList<>(board.getLegalMoves().values());

		for (PlayerMove move : moves) {
			Connect4Game newGame = (Connect4Game) game.copy();
			Connect4Board newC4Board = (Connect4Board) newGame.getGameBoard();

			if (parent != null) {
				newGame.moveNumber();
			}

			Player newPlayer = newGame.getPlayers().get(0)
					.getMover((newGame.queryMove() - 1) % newGame.getPlayers().size());
			newC4Board.setBoardSpace(move.getYCoordinate(), move.getXCoordinate(), (GamePieces) newPlayer);

			Tree child = new Tree(newGame, this, maxDepth);
			child.setMove(move);
			children.add(child);
		}

	}
	
	/**
	 * 
	 * Get the utility of this node plus the sum of all the utilities of it's descendants.
	 * 
	 * @return the sum of all the probabilities of this nodes and it's descendants.
	 */
	public Integer cumulativeUtility() {
		Integer sum = utility;
		for (Tree child : getChildren()) {
			sum += child.cumulativeUtility();
		}
		return sum;
	}

	// Display the tree using the indent. This method is for debugging and ought
	// to be deleted prior to release. For anything but small trees it will hose
	// your stdout.
	public void display(String indent) {
		System.out.println();
		if (parent == null) {
			System.out.println("Root of tree, player " + player + "'s turn");
		} else {
			System.out.println(indent + player + " moves to " + move + " for utility of " + cumulativeUtility()
					+ ". depth is " + getDepth() + ". minimax is " + minimax());
		}
		Connect4Board board = (Connect4Board) game.getGameBoard();
		board.display(indent);
		for (Tree tree : children) {
			tree.display("    " + indent);
		}

	}

	public List<Tree> getChildren() {
		return children;
	}

	public Integer getDepth() {
		return depth;
	}

	public Connect4Game getGame() {
		return game;
	}

	public Integer getMaxDepth() {
		return maxDepth;
	}

	public PlayerMove getMove() {
		return move;
	}

	public Tree getParent() {
		return parent;
	}

	public Player getPlayer() {
		return player;
	}

	public Integer getUtility() {
		return utility;
	}

	
	/**
	 * Perform the minimax algorithm on tree. Utilities for our player are positive, and for the opponent negative.
	 * 
	 * In general, a better minimax score here implies a better move to take.
	 * 
	 * @return the minimax scores at this node of the tree.
	 * 
	 * @see Tree#cumulativeUtility(), 
	 * 
	 */
	public Integer minimax() {

		if (getChildren().isEmpty()) {
			if (player == rootPlayer()) {
				return cumulativeUtility();
			} else {
				return cumulativeUtility() * -1;
			}
		}

		Integer sum = null;
		if (player == rootPlayer()) {
			sum = Integer.MAX_VALUE;
			for (Tree child : getChildren()) {
				sum = Math.min(sum, child.minimax());
			}
		} else {
			sum = Integer.MIN_VALUE;
			for (Tree child : getChildren()) {
				sum = Math.max(sum, child.minimax());
			}
		}
		return sum;
	}

	public Player rootPlayer() {
		if (parent == null) {
			return player;
		} else {
			return getParent().rootPlayer();
		}
	}

	public void setChildren(List<Tree> children) {
		this.children = children;
	}

	public void setGame(Connect4Game game) {
		this.game = game;
	}

	public void setMaxDepth(Integer maxDepth) {
		this.maxDepth = maxDepth;
	}

	public void setMove(PlayerMove move) {
		this.move = move;
	}

	public void setParent(Tree parent) {
		this.parent = parent;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setUtility(Integer utility) {
		this.utility = utility;
	}
}