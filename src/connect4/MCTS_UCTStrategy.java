package connect4;

import connectionAPI.Game;
import connectionAPI.Player;
import connectionAPI.PlayerMove;
import connectionAPI.Strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 *         <p/>
 *         UCT the upper bound as applied to trees: caculated
 *         (w_i / n_i) + c* sqrt(ln t / n_i)
 *         <p/>
 *         w_i = number of wins after ith move
 *         n_i = number of sims after the ith move
 *         c = constant chosen empirically (sqrt(2))
 *         t = total number of sim for this node
 */

public class MCTS_UCTStrategy implements Strategy {
    public static final String NAME = "Monte Carlo Tree Search using UCT(upper bound applied to trees)";
    public static final double UCT_CONSTANT = Math.sqrt(2.0);
    private int playDuration;
    private Random r;

    public MCTS_UCTStrategy(int playClock) {
        this.playDuration = playClock;
        r = new Random(System.currentTimeMillis());
    }

    /**
     * @return the name in plane english for the strategy
     */
    @Override
    public String getStrategyName() {
        return MCTS_UCTStrategy.NAME;
    }

    /**
     * @return playDuration: time allotted for MCTS to play random games
     */
    public int getPlayDuration() {
        return this.playDuration;
    }

    /**
     * This allows for different strategies to be played by different players
     *
     * @param game board being played
     * @return a move object
     */
    @Override
    public PlayerMove getNextMove(Game game) {
        long stop = System.currentTimeMillis() + this.playDuration;
        Connect4Game hypotheticalGame = (Connect4Game) game.copy();
        PlayerMove p;
//        Player thisPlayer;
//
//        // We need to know who is playing
//        thisPlayer = hypotheticalGame.getPlayers().get(0).getMover((hypotheticalGame.queryMove() - 1) % hypotheticalGame.getPlayers().size());

        // Check to see if i can win on the next move (queryMove-1) because the move has
        // already been played in the game the result of the move has not been determined
        p = canWin((Connect4Game) hypotheticalGame.copy(), hypotheticalGame.getPlayers().get(0).getMover((hypotheticalGame.queryMove() - 1) % hypotheticalGame.getPlayers().size()));
        if (p != null)
            return p;

        // Check to see if my opponent can win on the next move queryMove because
        // we are looking to see if our opponent can win on the next move
        p = canWin((Connect4Game) hypotheticalGame.copy(), hypotheticalGame.getPlayers().get(0).getMover(hypotheticalGame.queryMove() % hypotheticalGame.getPlayers().size()));
        if (p != null)
            return p;

        // Game tree for the board at this state with a newly created root node
        // some place for the game tree to start
        HashMap<Integer, Hashtable<String, Object>> gameTree = new HashMap<>();
        Hashtable<String, Object> root = new Hashtable<>();
        root.put("parent", hypotheticalGame.hashCode());
        root.put("game", hypotheticalGame);
        root.put("visitCount", 1);
        root.put("winCount", 0);
        gameTree.put(hypotheticalGame.hashCode(), root);

        int selectedNode = selection(gameTree, root);
        int expandedLeaf = expandTree(gameTree, selectedNode);
        Player winner = runSimulationFromLeaf(gameTree, expandedLeaf);
        return null;
    }

    private int selection(HashMap<Integer, Hashtable<String, Object>> gameTree, Hashtable<String, Object> parent) {

        Connect4Game thisGame = (Connect4Game) parent.get("game");
        ArrayList<Connect4Game> playedGames = new ArrayList<>();
        ArrayList<Connect4Game> notPlayedGames = new ArrayList<>();

        for (PlayerMove m : thisGame.getGameBoard().getLegalMoves().values()) {
            Connect4Game g = (Connect4Game) thisGame.copy();
            Player p = g.getPlayers().get(0).getMover((g.queryMove() - 1) % g.getPlayers().size());

            ((Connect4Board) g.getGameBoard()).setBoardSpace(m.getYCoordinate(), m.getXCoordinate(), (GamePieces) p);

            if (gameTree.containsKey(g.hashCode()))
                playedGames.add(g);
            else notPlayedGames.add(g);
        }

        if (!notPlayedGames.isEmpty())
            return thisGame.hashCode();
        else {
            double bestValue = Double.MIN_VALUE;
            int bestGameHash = 0;

            for (Connect4Game g : playedGames) {
                Hashtable<String, Object> h = gameTree.get(g.hashCode());
                double visits = (double) h.get("visitCount");
                double wins = (double) h.get("winCount");

                double value = (wins / visits) * UCT_CONSTANT * Math.sqrt(Math.log((double) parent.get("visitCount")) / visits);

                if (value > bestValue) {
                    bestValue = value;
                    bestGameHash = g.hashCode();
                }
            }

            // update the visit count for the selected node
            Hashtable<String, Object> h = gameTree.get(bestGameHash);
            h.put("visitCount", ((Integer) h.get("visitCount") + 1));
            return selection(gameTree, gameTree.get(bestGameHash));
        }
    }

    private int expandTree(HashMap<Integer, Hashtable<String, Object>> gameTree, int nodeKey) {
        // get the parent game
        Hashtable<String, Object> gameStatistics = gameTree.get(nodeKey);
        Connect4Game parentGame = (Connect4Game) gameStatistics.get("game");

        // get the new games from this game that have not yet been visited
        ArrayList<Connect4Game> notPlayedGames = new ArrayList<>();
        for (PlayerMove m : parentGame.getGameBoard().getLegalMoves().values()) {
            Connect4Game g = (Connect4Game) parentGame.copy();
            Player p = g.getPlayers().get(0).getMover((g.queryMove() - 1) % g.getPlayers().size());

            ((Connect4Board) g.getGameBoard()).setBoardSpace(m.getYCoordinate(), m.getXCoordinate(), (GamePieces) p);

            if (!gameTree.containsKey(g.hashCode()))
                notPlayedGames.add(g);
        }

        // from the list of possible nodes not yet in the tree pick a random one
        Connect4Game selectedGame = (Connect4Game) notPlayedGames.toArray()[r.nextInt(notPlayedGames.size())];
        // play the move to keep this value consistent though out the game we want the hash to be correct
        selectedGame.moveNumber();

        // add that game to the tree
        Hashtable<String, Object> newGameStatistics = new Hashtable<>();
        newGameStatistics.put("parent", nodeKey);
        newGameStatistics.put("game", selectedGame);
        newGameStatistics.put("visitCount", 1);
        newGameStatistics.put("winCount", 0);
        gameTree.put(selectedGame.hashCode(), newGameStatistics);

        return selectedGame.hashCode();
    }

    private Player runSimulationFromLeaf(HashMap<Integer, Hashtable<String, Object>> gameTree, int expandedLeaf) {
        return null;
    }

    private PlayerMove canWin(Connect4Game game, Player p) {

        for (PlayerMove move : game.getGameBoard().getLegalMoves().values()) {
            // Try each possible legal move
            ((Connect4Board) game.getGameBoard()).setBoardSpace(move.getYCoordinate(), move.getXCoordinate(), (GamePieces) p);

            if (((Connect4Board) game.getGameBoard()).isWon())
                return move;

            //Rest moves that don't win
            ((Connect4Board) game.getGameBoard()).setBoardSpace(move.getYCoordinate(), move.getXCoordinate(), GamePieces.EMPTY);
        }
        return null;
    }


    //    private int makeSelection(HashMap<Integer, Hashtable<String, Object>> gameTree, Hashtable<String, Object> parent) {
//
//        Connect4Game aGame = (Connect4Game) parent.get("game");
//        ArrayList<Connect4Game> playedGames = new ArrayList<>();
//        ArrayList<Connect4Game> notPlayedGames = new ArrayList<>();
//
//        for (PlayerMove m : aGame.getGameBoard().getLegalMoves().values()){
//            Connect4Game g = (Connect4Game) aGame.copy();
//            Player p = g.getPlayers().get(0).getMover((g.queryMove()-1) % g.getPlayers().size());
//
//            ((Connect4Board)g.getGameBoard()).setBoardSpace(m.getYCoordinate(), m.getYCoordinate(), (GamePieces) p);
//
//            if (gameTree.containsKey(g.hashCode()))
//                playedGames.add(g);
//            else notPlayedGames.add(g);
//        }
//
//        if (!notPlayedGames.isEmpty()){
//            Connect4Game selectedGame = (Connect4Game) notPlayedGames.toArray()[r.nextInt(notPlayedGames.size())];
//            Hashtable<String, Object> h = new Hashtable<>();
//            h.put("parent", parent);
//            h.put("game", selectedGame);
//            h.put("visitCount", 1);
//            h.put("winCount", 0);
//            gameTree.put(selectedGame.hashCode(), h);
//            return selectedGame.hashCode();
//        } else{
//            double bestValue = Double.MIN_VALUE;
//            int bestGameHash = 0;
//
//            for (Connect4Game g : playedGames){
//                Hashtable<String, Object> h = gameTree.get(g.hashCode());
//                double visits = (double) h.get("visitCount");
//                double wins = (double) h.get("winCount");
//
//                double value = (wins / visits) * UCT_CONSTANT * Math.sqrt(Math.log((double) parent.get("visitCount")) / visits);
//
//                if (value > bestValue) {
//                    bestValue = value;
//                    bestGameHash = g.hashCode();
//                }
//            }
//
//            return makeSelection(gameTree, gameTree.get(bestGameHash));
//        }
//    }
}