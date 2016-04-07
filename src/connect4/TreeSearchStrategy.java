package connect4;

import java.util.ArrayList;
import java.util.List;

import connectionAPI.Game;
import connectionAPI.Player;
import connectionAPI.PlayerMove;
import connectionAPI.Strategy;

public class TreeSearchStrategy implements Strategy {

	@Override
	public String getStrategyName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public PlayerMove getNextMove(Game game) {
		Player thisPlayer = game.getPlayers().get(0).getMover((game.queryMove() - 1) % game.getPlayers().size());

		Connect4Board c4board = (Connect4Board) game.getGameBoard();

		List<PlayerMove> moves = new ArrayList<>(c4board.getLegalMoves().values());

		Tree tree = new Tree((Connect4Game) game, 4);
		tree.display("");
		
		System.out.println("rootPlayer is " + tree.rootPlayer());
		
		for(Tree c: tree.getChildren()){
			System.out.println(c.getMove() + ": " +c.cumulativeUtility(thisPlayer));
		}
		
		for(Tree c: tree.getChildren()){
			System.out.println(c.getMove() + ": " +c.minimax());
		}
		
		return null;
	}

	public static void main(String[] args) {
		Connect4Game c4g;
		ArrayList<Player> players = new ArrayList<>();

		for (Player p : GamePieces.values()) {
			if (p == GamePieces.EMPTY)
				continue;
			if (p.turn() == 1)
				p.setPlayerStrategy(new InteractiveStrategy());
			if (p.turn() == 0)
				p.setPlayerStrategy(new TreeSearchStrategy());
			players.add(p);
		}

		// build a new game with players
		c4g = new Connect4Game(3, 4, players);
		
		((Connect4Board) c4g.getGameBoard()).setBoardSpace(0, 0, GamePieces.WHITE);
		((Connect4Board) c4g.getGameBoard()).setBoardSpace(0, 1, GamePieces.WHITE);
		((Connect4Board) c4g.getGameBoard()).setBoardSpace(1, 0, GamePieces.WHITE);
		((Connect4Board) c4g.getGameBoard()).setBoardSpace(0, 2, GamePieces.BLACK);
		((Connect4Board) c4g.getGameBoard()).setBoardSpace(0, 3, GamePieces.BLACK);
		((Connect4Board) c4g.getGameBoard()).setBoardSpace(1, 2, GamePieces.BLACK);
		((Connect4Board) c4g.getGameBoard()).setConnectionLength(3);
		c4g.play();
		System.out.println(((Connect4Board) c4g.getGameBoard()).getWinner());
		System.exit(0);
	}
}

class Tree {

	private Tree parent;
	private Connect4Game game;
	private List<Tree> children;
	private Player player;
	private PlayerMove move;
	private Double utility;
	private Integer depth;
	private Integer maxDepth;

	public Tree(Connect4Game game, Tree parent, Integer maxDepth) {
		this.game = game;
		this.parent = parent;
		this.children = new ArrayList<>();
		this.utility = 0.0d;
		this.maxDepth = maxDepth;
		this.player = game.getPlayers().get(0)
				.getMover((game.queryMove() - 1) % game.getPlayers().size());
		if(parent == null){
			depth = 0;
		} else {
			this.depth = parent.getDepth() + 1;
		}
		build();
	}
	
	public Tree(Connect4Game game, Tree parent){
		this(game, parent, Integer.MAX_VALUE);
	}

	public Tree(Connect4Game game) {
		this(game, (Tree) null, Integer.MAX_VALUE);
	}
	
	public Tree(Connect4Game game, Integer maxDepth) {
		this(game, (Tree) null, maxDepth);
	}

	private void build() {
		Connect4Board board = (Connect4Board) game.getGameBoard();
		System.out.println("getDepth()="+ getDepth());
		if(getDepth()==maxDepth){
			System.out.println("max depth!");
			setUtility(0.0d);
			return;
		}
		
		if(board.isWon()){
			if( board.getWinner() == rootPlayer()){
				setUtility(1.0d);
			} else {
				setUtility(-1.0d);
			}
			return;
		} 	
		if(board.isDraw()){
			setUtility(0.0d);
			return;
		} 
		
		
		List<PlayerMove> moves = new ArrayList<>(board.getLegalMoves().values());

		for (PlayerMove move : moves) {
			Connect4Game newGame = (Connect4Game) game.copy();
			Connect4Board newC4Board = (Connect4Board) newGame.getGameBoard();
			
			if(parent!=null){
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

	public Player rootPlayer(){
		if(parent == null){
			return player;
		} else {
			return getParent().rootPlayer();
		}
	}
	
	public Double minimax(){
			
		if(getChildren().isEmpty()){
			return cumulativeUtility(rootPlayer());
		}
		
		Double sum = null;
		if(player == rootPlayer()){
			sum = Double.MAX_VALUE;
			for(Tree child: getChildren()){
				sum = Math.min(sum, child.minimax());
			}
		} else {
			sum = Double.MAX_VALUE * -1.0;
			for(Tree child: getChildren()){
				sum = Math.max(sum, child.minimax());
			}
		}
		return sum;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public PlayerMove getMove() {
		return move;
	}

	public void setMove(PlayerMove move) {
		this.move = move;
	}

	public Double getUtility() {
		return utility;
	}
	
	public Double cumulativeUtility(Player player) {
		Double sum = utility;
		for(Tree child: getChildren()){
			sum+=child.cumulativeUtility(this.player);
		}
		return sum;
	}
	
	public void setUtility(Double utility) {
		this.utility = utility;
	}

	public Tree getParent() {
		return parent;
	}

	public void setParent(Tree parent) {
		this.parent = parent;
	}

	public Connect4Game getGame() {
		return game;
	}

	public void setGame(Connect4Game game) {
		this.game = game;
	}

	public List<Tree> getChildren() {
		return children;
	}

	public void setChildren(List<Tree> children) {
		this.children = children;
	}

	public void addChild(Tree child) {
		child.setParent(this);
		children.add(child);
	}
	
	public Integer getDepth() {
		return depth;
	}

	public void display(String indent) {
		System.out.println();
		if(parent == null){
			System.out.println("Root of tree, player " + player + "'s turn");
		} else {
			System.out.println(indent + player + " moves to " + move + " for utility of " + cumulativeUtility(rootPlayer()) + ". depth is " + getDepth() + ". minimax is " + minimax());
		}
		Connect4Board board = (Connect4Board) game.getGameBoard();
		board.display(indent);
		for (Tree tree : children) {
			tree.display("    " + indent);
		}

	}
}



class TerminalNodeEvaluator {
	public double evaluate(Tree terminal){
		return 0;
	}
}
