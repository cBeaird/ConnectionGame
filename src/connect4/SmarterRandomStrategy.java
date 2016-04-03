package connect4;

import connectionAPI.Game;
import connectionAPI.Player;
import connectionAPI.PlayerMove;
import connectionAPI.Strategy;

import java.util.Random;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 */

public class SmarterRandomStrategy implements Strategy {
    public static final String name = "Smarter random Strategy";

    public SmarterRandomStrategy() {
    }

    @Override
    public String getStrategyName() {
        return name;
    }

    @Override
    public PlayerMove getNextMove(Game game) {
        Connect4Game hypotheticalGame = (Connect4Game) game.copy();
        PlayerMove p;

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

        return (PlayerMove) hypotheticalGame.getGameBoard().getLegalMoves().values().toArray()[new Random().nextInt(hypotheticalGame.getGameBoard().getLegalMoves().size())];
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
}
