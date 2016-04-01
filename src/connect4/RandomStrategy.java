package connect4;

import connectionAPI.Game;
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

public class RandomStrategy implements Strategy {
    public static final String name = "Random Strategy";


    @Override
    public String getStrategyName() {
        return name;
    }

    /**
     * @param game board being played
     * @return choose a random move from the current boards legal moves
     */
    @Override
    public PlayerMove getNextMove(Game game) {
        return (PlayerMove) game.getGameBoard().getLegalMoves().values().toArray()[
                new Random().nextInt(game.getGameBoard().getLegalMoves().size())];
    }
}
