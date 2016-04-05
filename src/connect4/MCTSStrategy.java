package connect4;

import connectionAPI.*;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 */

public class MCTSStrategy implements Strategy {
    public static final String name = "Monte Carlo Tree Search pure random";
    private final int playDuration;

    public MCTSStrategy(int playClock) {
        this.playDuration = playClock;
    }

    @Override
    public String getStrategyName() {
        return name;
    }

    @Override
    public PlayerMove getNextMove(Game game) {
        long stop = System.currentTimeMillis() + this.playDuration;
        Connect4Game hypotheticalGame = (Connect4Game) game.copy();
        PlayerMove p;
        Player thisPlayer;

        // We need to know who is playing
        thisPlayer = hypotheticalGame.getPlayers().get(0).getMover((hypotheticalGame.queryMove() - 1) % hypotheticalGame.getPlayers().size());

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

        // Get a map of the already played moves as we are looking for the moves that
        // need to be played.
        HashMap<Integer, PlayerMove> history = new HashMap<>();
        for (int row = 0; row < game.getGameBoard().boardHeight(); row++) {
            for (int col = 0; col < game.getGameBoard().boardWidth(); col++) {
                PlayerMove m = ((Connect4Board) game.getGameBoard()).getBoardSpaceAsMove(row, col);
                if (m.getOwner() != GamePieces.EMPTY) {
                    history.put(m.hashCode(), m);
                }
            }
        }

        // There is no easy move so we need to find the best move with in the time limit
        HashMap<Integer, Hashtable<String, Object>> playedBoards = new HashMap<>();

        while (System.currentTimeMillis() < stop) {
            Connect4Board playedBoard = (Connect4Board) playARandomGameFromThisState(hypotheticalGame.copy());
            if (playedBoard.getWinner() != GamePieces.EMPTY) {
                Player winner = playedBoard.getWinner();
                for (int row = 0; row < playedBoard.boardHeight(); row++) {
                    for (int col = 0; col < playedBoard.boardWidth(); col++) {
                        PlayerMove m = playedBoard.getBoardSpaceAsMove(row, col);
                        if (m.getOwner() != GamePieces.EMPTY) {
                            if (!history.containsKey(m.hashCode())) {
                                if (!playedBoards.containsKey(m.hashCode())) {
                                    playedBoards.put(m.hashCode(), new Hashtable<String, Object>());
                                    playedBoards.get(m.hashCode()).put("move", m);
                                    playedBoards.get(m.hashCode()).put("visitCount", 0);
                                    playedBoards.get(m.hashCode()).put("winCount", 0);
                                }
                                Hashtable<String, Object> h = playedBoards.get(m.hashCode());
                                h.put("visitCount", (Integer) h.get("visitCount") + 1);
                                if (winner == thisPlayer) {
                                    h.put("winCount", (Integer) h.get("winCount") + 1);
                                } else {
                                    h.put("winCount", (Integer) h.get("winCount") - 1);
                                }
                            }
                        }
                    }
                }
            }
        }

        PlayerMove bestMove = null;
        int value = Integer.MIN_VALUE;
        for (PlayerMove m : hypotheticalGame.getGameBoard().getLegalMoves().values()) {
            m.setOwner(thisPlayer);
            if (playedBoards.containsKey(m.hashCode())) {
                Hashtable<String, Object> h = playedBoards.get(m.hashCode());
                if ((Integer) h.get("winCount") >= value) {
                    value = (Integer) h.get("winCount");
                    bestMove = (PlayerMove) h.get("move");
                }
            }
        }

        return bestMove;
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

    private Board playARandomGameFromThisState(Game randomGame) {
        HashMap<Integer, Strategy> originalStrategies = new HashMap<>();
        randomGame.setMoveNumber((randomGame.queryMove() - 1));

        for (Player p : randomGame.getPlayers()) {
            originalStrategies.put(p.turn(), p.getPlayerStrategy());
            p.setPlayerStrategy(new RandomStrategy());
        }

        ((Connect4Game) randomGame).playSilent();

        for (Player p : randomGame.getPlayers()) {
            p.setPlayerStrategy(originalStrategies.get(p.turn()));
        }
        return randomGame.getGameBoard();
    }
}
