package connectionAPI;

/**
 * @author Casey Beaird
 * @author Chris Everitt
 *         Created on 3/20/16.
 *         Virginia Commonwealth University
 *         Computer Science Department
 *         Course 612 Game Theory
 */

public interface Strategy {
    /**
     * @return the name in plane english for the strategy
     */
    String getStrategyName();

    /**
     * This allows for different strategies to be played by different players
     *
     * @param board game board being played
     * @return a move object
     */
    PlayerMove getNextMove(Board board);
}
